#!/bin/bash
# Run the translate over all inputfiles in src/test/resources/sitelog and output to /tmp/geodesyml/runall
# $1 - (OPTIONAL) input file to process - output written to $OUTPUTDIRTMP
# If no arg then processes all files in $INPUTDIR

INPUTDIR=src/test/resources/sitelog
OUTPUTDIR=$INPUTDIR/../sitelogtoGeodesyML
OUTPUTDIRTMP=/tmp/geodesyml/runall

if [ ! -d $OUTPUTDIR ]; then
    mkdir -p $OUTPUTDIR
fi
if [ ! -d $OUTPUTDIRTMP ]; then
    mkdir -p $OUTPUTDIRTMP
fi

if [ ! "x$1x"=="xx" ]; then
	if [ -e $1 ]; then
		 mvn exec:java -Dexec.mainClass="au.gov.ga.geodesy.support.commandline.GeodesyMLCommandLine" -Dexec.args="-infile $1 -outdir $OUTPUTDIRTMP"
	else
		echo ERROR: File to process specified on command-line but it doesnt exist: $1
		exit 1
	fi
else
	mvn exec:java -Dexec.mainClass="au.gov.ga.geodesy.support.commandline.GeodesyMLCommandLine" -Dexec.args="-indir $INPUTDIR -outdir $OUTPUTDIR"
fi
