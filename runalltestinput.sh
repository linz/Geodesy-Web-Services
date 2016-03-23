#!/bin/bash
# Run the translate over all inputfiles in src/test/resources/sitelog and output to /tmp/geodesyml/runall

INPUTDIR=src/test/resources/sitelog
OUTPUTDIR=/tmp/geodesyml/runall

if [ ! -d $OUTPUTDIR ]; then
    mkdir -p $OUTPUTDIR
fi

#for i in $INPUTDIR/*
#do
#    echo $i
    
mvn exec:java -Dexec.mainClass="au.gov.ga.geodesy.support.commandline.GeodesyMLCommandLine" -Dexec.args="-indir $INPUTDIR -outdir $OUTPUTDIR"
#    exit 1
#done
