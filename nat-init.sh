#direct external HTTP traffic to the webserver in the private subnet
# local_ip=`curl http://169.254.169.254/latest/meta-data/local-ipv4`
# iptables -t nat -A PREROUTING -i eth0 -p tcp -d $local_ip --dport 80 -j DNAT --to 10.0.1.100:80 && iptables-save > /etc/sysconfig/iptables
curl -X PUT -H 'Content-Type:' --data-binary '{"Status" : "SUCCESS","Reason" : "Configuration Complete","UniqueId" : "ID1234","Data" : "NAT is ready"}' "$signal_url"
