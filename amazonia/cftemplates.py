# pylint: disable=too-many-arguments, wildcard-import, unused-wildcard-import

""" Templates to implement common cloud formation configurations

The functions in this module generate cloud formation scripts that install common AWS environments and components

"""

from amazonia.amazonia_resources import *
from troposphere import Template


def addVPC(template):
    """Create a VPC resource and add it to the given template."""
    vpc = add_vpc(template, VPC_CIDR)
    return vpc

class SingleAZenv(Template):

   def __init__(self, key_pair_name):
    """ Public Class to create a single AZ environment in a vpc """
    super(SingleAZenv,self).__init__()
    self.vpc = addVPC(self)

    # configure network
    self.public_subnet = add_subnet(self, self.vpc, PUBLIC_SUBNET_NAME, PUBLIC_SUBNET_AZ1_CIDR)
    public_route_table = add_route_table(self, self.vpc, "Public")
    add_route_table_subnet_association (self, public_route_table, self.public_subnet)

    internet_gateway = add_internet_gateway(self, self.vpc)
    add_route_ingress_via_gateway(self, public_route_table, internet_gateway, PUBLIC_CIDR)
    self.private_subnet = add_subnet(self, self.vpc, PRIVATE_SUBNET_NAME, PRIVATE_SUBNET_AZ1_CIDR)
    private_route_table = add_route_table(self, self.vpc, "Private")
    add_route_table_subnet_association (self, private_route_table, self.private_subnet)

    #NAT Security Group
    nat_sg = add_security_group(self, self.vpc)
    # enable inbound http access to the NAT from anywhere
    add_security_group_ingress(self, nat_sg, 'tcp', '80', '80', cidr=PUBLIC_CIDR)
    # enable inbound https access to the NAT from anywhere
    add_security_group_ingress(self, nat_sg, 'tcp', '443', '443', cidr=PUBLIC_CIDR)
    # enable inbound SSH  access to the NAT from GA
    add_security_group_ingress(self, nat_sg, 'tcp', '22', '22', cidr=PUBLIC_GA_GOV_AU_CIDR)
    # enable inbound ICMP access to the NAT from anywhere
    add_security_group_ingress(self, nat_sg, 'icmp', '-1', '-1', cidr=PUBLIC_CIDR)

    self.nat = add_nat(self, self.public_subnet, key_pair_name, nat_sg)
    add_route_egress_via_NAT(self, private_route_table, self.nat)

class DualAZenv(Template):

    def __init__(self, key_pair_name):
        """ Public Class to create a dual AZ environment in a vpc """
        super(DualAZenv, self).__init__()
        self.vpc = addVPC(self)

        # AZ 1
        self.public_subnet1 = add_subnet(self, self.vpc, PUBLIC_SUBNET_NAME, PUBLIC_SUBNET_AZ1_CIDR)
        self.public_route_table1 = add_route_table(self, self.vpc, "Public")
        add_route_table_subnet_association(self, self.public_route_table1, self.public_subnet1)
        self.internet_gateway = add_internet_gateway(self, self.vpc)
        add_route_ingress_via_gateway(self, self.public_route_table1, self.internet_gateway, PUBLIC_CIDR)
        self.private_subnet1 = add_subnet(self, self.vpc, PRIVATE_SUBNET_NAME, PRIVATE_SUBNET_AZ1_CIDR)
        self.private_route_table1 = add_route_table(self, self.vpc, "Private")
        add_route_table_subnet_association(self, self.private_route_table1, self.private_subnet1)

        # NAT Security Group
        self.nat_security_group = add_security_group(self, self.vpc)
        # enable inbound http access to the NAT from anywhere
        add_security_group_ingress(self, self.nat_security_group, 'tcp', '80', '80', cidr=PUBLIC_CIDR)
        # enable inbound https access to the NAT from anywhere
        add_security_group_ingress(self, self.nat_security_group, 'tcp', '443', '443', cidr=PUBLIC_CIDR)
        # enable inbound SSH  access to the NAT from GA
        add_security_group_ingress(self, self.nat_security_group, 'tcp', '22', '22', cidr=PUBLIC_GA_GOV_AU_CIDR)
        # enable inbound ICMP access to the NAT from anywhere
        add_security_group_ingress(self, self.nat_security_group, 'icmp', '-1', '-1', cidr=PUBLIC_CIDR)

        self.nat = add_nat(self, self.public_subnet1, key_pair_name, self.nat_security_group)
        add_route_egress_via_NAT(self, self.private_route_table1, self.nat)

        switch_availability_zone()

        # AZ 2
        self.public_subnet2 = add_subnet(self, self.vpc, PUBLIC_SUBNET_NAME, PUBLIC_SUBNET_AZ2_CIDR)
        # Note below how we associate public subnet 2 to the single public route table we create for the VPC
        add_route_table_subnet_association(self, self.public_route_table1, self.public_subnet2)

        self.private_subnet2 = add_subnet(self, self.vpc, PRIVATE_SUBNET_NAME, PRIVATE_SUBNET_AZ2_CIDR)
        self.private_route_table2 = add_route_table(self, self.vpc, "Private")
        add_route_table_subnet_association(self, self.private_route_table2, self.private_subnet2)

        self.nat = add_nat(self, self.public_subnet2, key_pair_name, self.nat_security_group)
        add_route_egress_via_NAT(self, self.private_route_table2, self.nat)

        # Web Security Group
        self.web_security_group = add_security_group(self, self.vpc)
        add_security_group_ingress(self, self.web_security_group, 'tcp', '80', '80', cidr=PUBLIC_CIDR)
        add_security_group_ingress(self, self.web_security_group, 'tcp', '443', '443', cidr=PUBLIC_CIDR)
        add_security_group_ingress(self, self.web_security_group, 'tcp', '22', '22', cidr=PUBLIC_GA_GOV_AU_CIDR)

        self.web_instance1 = add_web_instance(self, key_pair_name, self.public_subnet1, self.web_security_group, WEB_SERVER_AZ1_USER_DATA)
        self.web_instance2 = add_web_instance(self, key_pair_name, self.public_subnet2, self.web_security_group, WEB_SERVER_AZ2_USER_DATA)

        self.load_balancer = add_load_balancer(self, [self.web_instance1, self.web_instance2], [self.public_subnet1, self.public_subnet2], "HTTP:80/error/noindex.html", [self.web_security_group])
