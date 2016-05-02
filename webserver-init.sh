#cloud-config

hostname: webserver

runcmd:
- /opt/aws/bin/cfn-init --region ap-southeast-2 -s GeodesyWebServices%(env)s -r WebserverLaunchConfig
