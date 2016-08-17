stack.template: geodesy.yaml geodesy_ga_defaults.yaml
	python3 amazonia/amazonia/amz.py -y geodesy.yaml -d geodesy_ga_defaults.yaml

%.jpg: %.template
	cat $< | cfviz | dot -Tjpg -o$@

war-file:
	mvn package -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true

.PHONEY:
stack: stack.template
	AWS_PROFILE=geodesy aws cloudformation create-stack --stack-name ${ENV} --template-body file://stack.template --parameters \
    ParameterKey=GeodesyGeodesyDbRdsUsername,ParameterValue=$(credstash get geodesy-db-master-username) \
    ParameterKey=GeodesyGeodesyDbRdsPassword,ParameterValue=$(credstash get geodesy-db-master-password)

.PHONEY:
restack: stack.template
	AWS_PROFILE=geodesy aws cloudformation update-stack --stack-name ${ENV} --template-body file://stack.template --parameters \
    ParameterKey=GeodesyGeodesyDbRdsUsername,ParameterValue=$(credstash get geodesy-db-master-username) \
    ParameterKey=GeodesyGeodesyDbRdsPassword,ParameterValue=$(credstash get geodesy-db-master-password)

.PHONEY:
unstack:
	AWS_PROFILE=geodesy aws cloudformation delete-stack --stack-name GeodesyWebServices${ENV}

.PHONEY:
viz: stack.jpg
	xv stack.jpg

.PHONEY:
clean:
	rm -f stack.json stack.jpg *.war
	mvn clean
