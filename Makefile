.PHONEY:
build:
	python setup.py build

singleAZ.json: amazonia/__init__.py amazonia/cftemplates.py
	python ./scripts/singleAZ > $@

dualAZ.json: amazonia/__init__.py amazonia/cftemplates.py
	python ./scripts/dualAZ_tests > $@

cftemplate_tests.json: amazonia/__init__.py amazonia/cftemplates.py
	source ./scripts/cftemplate_tests > $@

default_vpc.json: amazonia/__init__.py amazonia/cftemplates.py
	source ./scripts/viz > $@

%.svg: %.json
	cat $< | cfviz | dot -Tsvg -o$@

.PHONEY:
viz: default_vpc.svg
	feh --magick-timeout 1 $< &

.PHONEY:
install:
	pip install -e . --user

.PHONEY:
uninstall:
	yes | pip uninstall amazonia

.PHONEY:
clean:
	rm -rf *.dot *.svg *.json build dist *.egg-info
