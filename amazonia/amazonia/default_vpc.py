# pylint: disable=too-many-arguments

"""Default VPC definition

This is a VPC with an internet gateway and public and private subnets.
A NAT instance in the public subnet allows incoming SSH traffic from GA, and
HTTP and ICMP traffic from everywhere. The private subnet routes out all
outbound traffic via the NAT instance and the internet gateway.
"""

from . import name_tag, http_ingress, https_ingress, icmp_ingress, ssh_ingress_from_ga
from troposphere import Ref, Tags
import troposphere.ec2 as ec2

DEFAULT_NAT_IMAGE_ID = "ami-893f53b3"
DEFAULT_NAT_INSTANCE_TYPE = "t2.micro"

def add_vpc(template, key_pair_name, nat_ip,
            nat_image_id=DEFAULT_NAT_IMAGE_ID,
            nat_instance_type=DEFAULT_NAT_INSTANCE_TYPE):
    """Create a VPC resource and add it to the given template."""
    vpc_id = "VPC"
    vpc = template.add_resource(ec2.VPC(
        vpc_id,
        CidrBlock="10.0.0.0/16",
        Tags=Tags(
            Name=name_tag(vpc_id)
        ),
    ))
    public_subnet = _add_public_subnet(template, vpc)
    nat = _add_nat(template, vpc, public_subnet, nat_image_id, nat_instance_type,
                   key_pair_name, nat_ip)
    _add_private_subnet(template, vpc, nat)
    return vpc

def private_subnet(template):
    """Extract and return the "PublicSubnet" resource from the given template."""
    return template.resources["PrivateSubnet"]

def nat_instance(template):
    """Extract and return the NAT instance from the given template."""
    return template.resources["NAT"]

def _add_nat(template, vpc, public_subnet, image_id, instance_type, key_pair_name, nat_ip):
    nat_sg_id = "NatSecurityGroup"
    nat_sg = template.add_resource(ec2.SecurityGroup(
        nat_sg_id,
        GroupDescription="Security group for NAT instance",
        VpcId=Ref(vpc.title),
        Tags=Tags(
            Name=name_tag(nat_sg_id)
        ),
    ))
    template.add_resource(ssh_ingress_from_ga(nat_sg))
    template.add_resource(icmp_ingress(nat_sg))
    template.add_resource(http_ingress(nat_sg))
    template.add_resource(https_ingress(nat_sg))
    nat_id = "NAT"
    nat = template.add_resource(ec2.Instance(
        nat_id,
        SecurityGroupIds=[Ref(nat_sg.title)],
        KeyName=key_pair_name,
        SubnetId=Ref(public_subnet.title),
        ImageId=image_id,
        InstanceType=instance_type,
        SourceDestCheck=False,
        PrivateIpAddress="10.0.0.100",
        Tags=Tags(
            Name=name_tag(nat_id),
        ),
    ))
    template.add_resource(ec2.EIPAssociation(
        nat.title + "IpAssociation",
        EIP=nat_ip,
        InstanceId=Ref(nat.title)
    ))
    return nat

def _add_public_subnet(template, vpc):
    title = "PublicSubnet"
    public_subnet = template.add_resource(ec2.Subnet(
        title,
        VpcId=Ref(vpc.title),
        CidrBlock="10.0.0.0/24",
        Tags=Tags(
            Name=name_tag(title)
        ),
    ))
    internet_gateway = _add_internet_gateway(template, vpc)

    route_table_id = "PublicRouteTable"
    route_table = template.add_resource(ec2.RouteTable(
        route_table_id,
        VpcId=Ref(vpc.title),
        Tags=Tags(
            Name=name_tag(route_table_id),
        ),
    ))
    template.add_resource(ec2.SubnetRouteTableAssociation(
        "PublicRouteTableAssociation",
        SubnetId=Ref(public_subnet.title),
        RouteTableId=Ref(route_table),
    ))
    template.add_resource(ec2.Route(
        "InboundRoute",
        GatewayId=Ref(internet_gateway.title),
        RouteTableId=Ref(route_table.title),
        DestinationCidrBlock="0.0.0.0/0",
    ))
    return public_subnet

def _add_private_subnet(template, vpc, nat):
    title = "PrivateSubnet"
    subnet = template.add_resource(ec2.Subnet(
        title,
        VpcId=Ref(vpc.title),
        CidrBlock="10.0.1.0/24",
        Tags=Tags(
            Name=name_tag(title)
        ),
    ))
    route_table_id = "PrivateRouteTable"
    route_table = template.add_resource(ec2.RouteTable(
        route_table_id,
        VpcId=Ref(vpc.title),
        Tags=Tags(
            Name=name_tag(title),
        ),
    ))
    template.add_resource(ec2.SubnetRouteTableAssociation(
        "PrivateRouteTableAssociation",
        SubnetId=Ref(subnet.title),
        RouteTableId=Ref(route_table),
    ))
    template.add_resource(ec2.Route(
        "OutboundRoute",
        InstanceId=Ref(nat.title),
        RouteTableId=Ref(route_table.title),
        DestinationCidrBlock="0.0.0.0/0",
    ))
    return private_subnet


def _add_internet_gateway(template, vpc):
    gateway_id = "InternetGateway"
    internet_gateway = template.add_resource(ec2.InternetGateway(
        gateway_id,
        Tags=Tags(
            Name=name_tag(gateway_id),
        ),
    ))
    attachment_id = "InternetGatewayAttachment"
    template.add_resource(ec2.VPCGatewayAttachment(
        attachment_id,
        VpcId=Ref(vpc.title),
        InternetGatewayId=Ref(internet_gateway.title),
    ))
    return internet_gateway

