stack.json: stack.py webserver-init.sh nat-init.sh war-file
	python stack.py ${GEODESY_WEB_SERVICES_VERSION} ${ENV} > $@

%.jpg: %.json
	cat $< | cfviz | dot -Tjpg -o$@

war-file:
	mvn package -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true

.PHONEY:
stack: stack.json
	AWS_PROFILE=geodesy aws cloudformation create-stack --stack-name GeodesyWebServices${ENV} --template-body file://stack.json

.PHONEY:
restack: stack.json
	AWS_PROFILE=geodesy aws cloudformation update-stack --stack-name GeodesyWebServices${ENV} --template-body file://stack.json

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
