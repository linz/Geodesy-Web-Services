# pylint: disable=too-many-arguments, line-too-long

"""

The functions in this module generate cloud formation scripts that install common AWS environments and components

"""

from troposphere import Ref, Tags, Join, Base64, GetAtt
from troposphere.autoscaling import AutoScalingGroup, LaunchConfiguration, Tag
import troposphere.ec2 as ec2
import troposphere.elasticloadbalancing as elb

NAT_IMAGE_ID = "ami-893f53b3"
NAT_INSTANCE_TYPE = "t2.micro"
NAT_IP_ADDRESS = "10.0.0.100"
SYSTEM_NAME = "TestApplication"
ENVIRONMENT_NAME = "Experimental"
AVAILABILITY_ZONES = ["ap-southeast-2a", "ap-southeast-2b"]
WEB_IMAGE_ID = "ami-c11856fb"
WEB_INSTANCE_TYPE = "t2.small"
ASG_MIN_INSTANCES = 1

# CIDRs
PUBLIC_GA_GOV_AU_CIDR = '192.104.44.129/32'
VPC_CIDR = "10.0.0.0/16"
PUBLIC_SUBNET_AZ1_CIDR = "10.0.0.0/24"
PUBLIC_SUBNET_AZ2_CIDR = "10.0.10.0/24"
PRIVATE_SUBNET_AZ1_CIDR = "10.0.1.0/24"
PRIVATE_SUBNET_AZ2_CIDR = "10.0.11.0/24"
PUBLIC_CIDR = "0.0.0.0/0"
PUBLIC_SUBNET_NAME = "PublicSubnet"
PRIVATE_SUBNET_NAME = "PrivateSubnet"

# WEB SERVER BOOTSTRAP SCRIPTS
WEB_SERVER_AZ1_USER_DATA = "#!/bin/sh\n"
WEB_SERVER_AZ1_USER_DATA += "yum -y install httpd && chkconfig httpd on\n"
WEB_SERVER_AZ1_USER_DATA += "/etc/init.d/httpd start && yum -y install git\n"
WEB_SERVER_AZ1_USER_DATA += "git clone https://github.com/budawangbill/webserverconfig.git\n"
WEB_SERVER_AZ1_USER_DATA += "cp webserverconfig/testAZ1.html /var/www/html/test.html"

WEB_SERVER_AZ2_USER_DATA = "#!/bin/sh\n"
WEB_SERVER_AZ2_USER_DATA += "yum -y install httpd && chkconfig httpd on\n"
WEB_SERVER_AZ2_USER_DATA += "/etc/init.d/httpd start && yum -y install git\n"
WEB_SERVER_AZ2_USER_DATA += "git clone https://github.com/budawangbill/webserverconfig.git\n"
WEB_SERVER_AZ2_USER_DATA += "cp webserverconfig/testAZ2.html /var/www/html/test.html"

# Handler for switching Availability Zones
current_az = 0

# numbers to count objects created
num_vpcs = 0
num_subnets = 0
num_route_tables = 0
num_internet_gateways = 0
num_routes = 0
num_nats = 0
num_security_groups = 0
num_ingress_rules = 0
num_egress_rules = 0
num_load_balancers = 0
num_launch_configs = 0
num_web_instances = 0
num_web_security_groups = 0
num_route_table_associations = 0
num_auto_scaling_groups = 0

def switch_availability_zone():
    """ A simple function to switch Availability zones. """
    global current_az
    if current_az == 0:
        current_az = 1
    else:
        current_az = 0

def add_vpc(template, cidr):
    """Create a VPC resource and add it to the given template."""
    global num_vpcs
    num_vpcs += 1
    vpc_title = "VPC" + str(num_vpcs)

    vpc = template.add_resource(ec2.VPC(vpc_title,
                                        CidrBlock=cidr,
                                        Tags=Tags(Name=name_tag(vpc_title),
                                                  Environment=ENVIRONMENT_NAME)))
    return vpc


def add_subnet(template, vpc, name, cidr):
    global num_subnets
    num_subnets += 1

    title = name + str(num_subnets)
    public_subnet = template.add_resource(ec2.Subnet(title,
                                                     AvailabilityZone=AVAILABILITY_ZONES[current_az],
                                                     VpcId=Ref(vpc.title),
                                                     CidrBlock=cidr,
                                                     Tags=Tags(Name=name_tag(title),
                                                               Environment=ENVIRONMENT_NAME)))
    return public_subnet


def add_route_table(template, vpc, route_type=""):
    global num_route_tables
    num_route_tables = num_route_tables + 1

    # create the route table in the VPC
    route_table_id = route_type + "RouteTable" + str(num_route_tables)
    route_table = template.add_resource(ec2.RouteTable(route_table_id,
                                                       VpcId=Ref(vpc.title),
                                                       Tags=Tags(Name=name_tag(route_table_id)),
                                                      ))
    return route_table

def add_route_table_subnet_association(template, route_table, subnet):
    global num_route_table_associations
    num_route_table_associations += 1

    # Associate the route table with the subnet
    template.add_resource(ec2.SubnetRouteTableAssociation(
        route_table.title + "Association" + str(num_route_table_associations),
        SubnetId=Ref(subnet.title),
        RouteTableId=Ref(route_table.title),
    ))


def add_internet_gateway(template, vpc):
    global num_internet_gateways
    num_internet_gateways = num_internet_gateways + 1
    internet_gateway_title = "InternetGateway" + str(num_internet_gateways)

    internet_gateway = template.add_resource(ec2.InternetGateway(internet_gateway_title,
                                                                 Tags=Tags(Name=name_tag(internet_gateway_title),
                                                                           Environment=ENVIRONMENT_NAME)
                                                                ))

    attachment_title = internet_gateway_title + "Attachment"
    template.add_resource(ec2.VPCGatewayAttachment(attachment_title,
                                                   VpcId=Ref(vpc.title),
                                                   InternetGatewayId=Ref(internet_gateway.title),
                                                  ))
    return internet_gateway


def add_route_ingress_via_gateway(template, route_table, internet_gateway, cidr):
    global num_routes
    num_routes += 1
    template.add_resource(ec2.Route(
        "InboundRoute" + str(num_routes),
        GatewayId=Ref(internet_gateway.title),
        RouteTableId=Ref(route_table.title),
        DestinationCidrBlock=cidr
    ))


def add_route_egress_via_NAT(template, route_table, nat):
    global num_routes
    num_routes += 1

    template.add_resource(ec2.Route("OutboundRoute" + str(num_routes),
                                    InstanceId=Ref(nat.title),
                                    RouteTableId=Ref(route_table.title),
                                    DestinationCidrBlock="0.0.0.0/0",
                                   ))


def add_security_group(template, vpc):
    global num_security_groups
    num_security_groups += 1
    sg_title = "SecurityGroup" + str(num_security_groups)

    sg = template.add_resource(ec2.SecurityGroup(sg_title,
                                                 GroupDescription="Security group",
                                                 VpcId=Ref(vpc.title),
                                                 Tags=Tags(Name=name_tag(sg_title))))

    return sg



def add_security_group_ingress(template, security_group, protocol, from_port, to_port, cidr="", source_security_group=""):

    global num_ingress_rules
    num_ingress_rules += 1
    title = security_group.title + 'Ingress' + protocol + str(num_ingress_rules)
    sg_ingress = template.add_resource(ec2.SecurityGroupIngress(title,
                                                                IpProtocol=protocol,
                                                                FromPort=from_port,
                                                                ToPort=to_port,
                                                                GroupId=Ref(security_group.title)
                                                               ))

    if not source_security_group == "":
        sg_ingress.SourceSecurityGroupId = GetAtt(source_security_group.title, "GroupId")
    else:
        if not cidr == "":
            sg_ingress.CidrIp = cidr

    return sg_ingress

def add_security_group_egress(template, security_group, protocol, from_port, to_port, cidr="", destination_security_group=""):

    global num_egress_rules
    num_egress_rules += 1
    title = security_group.title + 'Egress' + protocol + str(num_egress_rules)
    sg_egress = template.add_resource(ec2.SecurityGroupEgress(title,
                                                              IpProtocol=protocol,
                                                              FromPort=from_port,
                                                              ToPort=to_port,
                                                              GroupId=Ref(security_group.title)
                                                             ))

    if not destination_security_group == "":
        sg_egress.DestinationSecurityGroupId = GetAtt(destination_security_group.title, "GroupId")
    else:
        if not cidr == "":
            sg_egress.CidrIp = cidr

    return sg_egress


def add_nat(template, public_subnet, key_pair_name, security_group, natIP=NAT_IP_ADDRESS):
    global num_nats
    num_nats += 1
    nat_title = "NAT" + str(num_nats)
    nat = template.add_resource(ec2.Instance(
        nat_title,
        KeyName=key_pair_name,
        ImageId=NAT_IMAGE_ID,
        InstanceType=NAT_INSTANCE_TYPE,
        NetworkInterfaces=[ec2.NetworkInterfaceProperty(
            GroupSet=[Ref(security_group.title)],
            AssociatePublicIpAddress=True,
            DeviceIndex="0",
            DeleteOnTermination=True,
            SubnetId=Ref(public_subnet.title),
        )],
        SourceDestCheck=False,
        Tags=Tags(
            Name=name_tag(nat_title),
        ),
    ))
    return nat

def add_web_instance(template, key_pair_name, subnet, security_group, userdata):
    global num_web_instances
    num_web_instances += 1

    instance_title = "WebServer" + str(num_web_instances)

    instance = template.add_resource(ec2.Instance(
        instance_title,
        InstanceType=WEB_INSTANCE_TYPE,
        KeyName=key_pair_name,
        SourceDestCheck=False,
        ImageId=WEB_IMAGE_ID,
        NetworkInterfaces=[ec2.NetworkInterfaceProperty(
            GroupSet=[Ref(security_group.title)],
            AssociatePublicIpAddress=True,
            DeviceIndex="0",
            DeleteOnTermination=True,
            SubnetId=Ref(subnet.title),
        )],
        Tags=Tags(
            Name=name_tag(instance_title),
        ),
        UserData=Base64(userdata),
    ))
    return instance


def get_refs(items):
    refs = []
    for item in items:
        refs.append(Ref(item.title))

    return refs

def get_titles(items):
    titles = []
    for item in items:
        titles.append(item.title)

    return titles


def add_load_balancer(template, subnets, healthcheck_target, security_groups, resources=""):
    global num_load_balancers
    num_load_balancers += 1

    subnet_refs = get_refs(subnets)
    security_group_refs = get_refs(security_groups)

    elb_title = "ElasticLoadBalancer" + str(num_load_balancers)
    return_elb = template.add_resource(elb.LoadBalancer(
        elb_title,
        CrossZone=True,
        HealthCheck=elb.HealthCheck(
            Target=healthcheck_target,
            HealthyThreshold="10",
            UnhealthyThreshold="2",
            Interval="30",
            Timeout="5",
        ),
        Listeners=[elb.Listener(
            LoadBalancerPort="80",
            Protocol="HTTP",
            InstancePort="80",
            InstanceProtocol="HTTP",
        )],
        Scheme="internet-facing",
        SecurityGroups=security_group_refs,
        Subnets=subnet_refs,
        Tags=Tags(
            Name=name_tag(elb_title),
        ),
    ))

    if not resources == "":
        resource_refs = get_refs(resources)
        return_elb.Instances = resource_refs

    return return_elb

def add_auto_scaling_group(template, health_check_type, launch_configuration, max_instances, load_balancer, subnets, dependson="", multiAZ=False):
    global num_auto_scaling_groups
    num_auto_scaling_groups += 1

    subnet_refs = get_refs(subnets)

    auto_scaling_group_title = "AutoScalingGroup" + str(num_auto_scaling_groups)

    asg = template.add_resource(AutoScalingGroup(
        auto_scaling_group_title,
        HealthCheckType=health_check_type,
        LaunchConfigurationName=Ref(launch_configuration.title),
        MinSize=ASG_MIN_INSTANCES,
        MaxSize=max_instances,
        LoadBalancerNames=[Ref(load_balancer.title)],
        VPCZoneIdentifier=subnet_refs,
        Tags=[
            Tag("Name", name_tag(auto_scaling_group_title), True)
        ],
    ))

    if multiAZ:
        asg.AvailabilityZones = AVAILABILITY_ZONES
    else:
        asg.AvailabilityZones = [AVAILABILITY_ZONES[current_az]]

    if health_check_type == "ELB":
        asg.HealthCheckGracePeriod = 600

    if not dependson == "":
        dependson_titles = get_titles(dependson)
        asg.DependsOn = dependson_titles

    return asg

def add_launch_config(template, key_pair_name, security_groups, userdata=""):
    global num_launch_configs
    num_launch_configs += 1

    launch_config_title = "LaunchConfiguration" + str(num_launch_configs)

    sg_refs = get_refs(security_groups)

    lc = template.add_resource(LaunchConfiguration(
        launch_config_title,
        AssociatePublicIpAddress=True,
        ImageId=WEB_IMAGE_ID,
        InstanceMonitoring=False,
        InstanceType=WEB_INSTANCE_TYPE,
        KeyName=key_pair_name,
        SecurityGroups=sg_refs,
    ))

    if not userdata == "":
        lc.UserData = Base64(userdata)
    return lc

def stack_name_tag():
    return "Ref('AWS::StackName')"

def name_tag(resource_name):
    """Prepend stack name to the given resource name."""
    return Join("", [Ref('AWS::StackName'), '-', resource_name])

def private_subnet(template, name):
    """Extract and return the specified subnet resource from the given template."""
    return template.resources[name]
