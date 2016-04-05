#!/bin/bash
# Run the translate over all inputfiles in src/test/resources/sitelog and output to /tmp/geodesyml/runall
#
# Arguments:
#   -i input directory (defaults to INPUTDIR=src/test/resources/sitelog)
#   -f input file
#   -o output directory (defaults to OUTPUTDIR=$INPUTDIR/../sitelogtoGeodesyML if -i or TMPOUTPUTDIR=/tmp/geodesyml/runall if -f)
#
# Arguments are all optional and if none then INPUTDIR and OUTPUTDIR is used as shown above.
#
# -f trumps -i
#
# The ability to choose an output directory is because the default is a git managed directory, which means that after every
#   time the script is run, a new set of files (653 of them!) have been modified and you feel the need to commit them
#   so you don't see 653 modified files.


INPUTDIR=src/test/resources/sitelog
OUTPUTDIR=$INPUTDIR/../sitelogtoGeodesyML
TMPOUTPUTDIR=/tmp/geodesyml/runall
# OUTDIR= - not set yet
DEFAULTOUTDIR=$OUTPUTDIR

while getopts i:f:o: opt
do
    case $opt in
        i)
            INPUTDIR=$OPTARG
            DEFAULTOUTDIR=$OUTPUTDIR
            ;;
        f) 
            INFILE=$OPTARG
            unset INPUTDIR
            DEFAULTOUTDIR=$TMPOUTPUTDIR
            ;;
        o)
            OUTDIR=$OPTARG
            ;;
    esac
done

if [[ -n $INPUTDIR ]] && [[ -n $INFILE ]]; then
    unset INPUTDIR
    DEFAULTOUTDIR=$TMPOUTPUTDIR
fi

if [ -z $OUTDIR ]; then
    OUTDIR=$DEFAULTOUTDIR
fi

echo $0 - INFILE: \"$INFILE\", INPUTDIR: \"$INPUTDIR\", OUTDIR: \"$OUTDIR\"

if [ ! -d $OUTPUTDIR ]; then
    mkdir -p $OUTPUTDIR
fi

if [[ -z $INFILE ]]; then
    mvn exec:java -Dexec.mainClass="au.gov.ga.geodesy.support.commandline.GeodesyMLCommandLine" -Dexec.args="-indir $INPUTDIR -outdir $OUTDIR"
else
    if [ -e $INFILE ]; then
         mvn exec:java -Dexec.mainClass="au.gov.ga.geodesy.support.commandline.GeodesyMLCommandLine" -Dexec.args="-infile $INFILE -outdir $OUTDIR"
    else
        echo ERROR: File to process specified on command-line but it doesnt exist: $INFILE
        exit 1
    fi
fi

echo $0 - INFILE: \"$INFILE\", INPUTDIR: \"$INPUTDIR\", OUTDIR: \"$OUTDIR\"
