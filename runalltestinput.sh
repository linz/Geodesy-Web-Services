#!/bin/bash
# Run the translate over all inputfiles in src/test/resources/sitelog and output to /tmp/geodesyml/runall

INPUTDIR=src/test/resources/sitelog
OUTPUTDIR=src/test/resources/sitelog/sitelogtoGeodesyML
OUTPUTDIRTMP=/tmp/geodesyml/runall

if [ ! -d $OUTPUTDIR ]; then
    mkdir -p $OUTPUTDIR
fi

mvn exec:java -Dexec.mainClass="au.gov.ga.geodesy.support.commandline.GeodesyMLCommandLine" -Dexec.args="-indir $INPUTDIR -outdir $OUTPUTDIR"
