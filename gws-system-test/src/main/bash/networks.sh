#!/usr/bin/env bash

set -e

# gws=http://localhost:8081
# openam=http://localhost:8083/openam

gws=https://gws.geodesy.ga.gov.au
openam=https://prodgeodesy-openam.geodesy.ga.gov.au/openam

clientId=GnssSiteManager
clientPassword=

username=
password=

jwt=$(curl -s --user ${clientId}:${clientPassword} --data "grant_type=password&username=${username}&password=${password}&scope=openid profile" ${openam}/oauth2/access_token?realm=/ | jq .id_token | tr -d '"')

function addSiteToNetwork {
    fourCharId=${1}
    shift

    addToNetwork=$(curl -s ${gws}/corsSites?fourCharacterId=${fourCharId} | jq ._embedded.corsSites[0]._links.addToNetwork.href | tr -d '"')

    if [ "${addToNetwork}" = "null" ]; then
        echo "No site ${fourCharId}"
        return
    fi

    for networkName in "$@"; do
        networkId=$(curl -s ${gws}/corsNetworks?name=${networkName} | jq ._embedded.corsNetworks[0].id)

        if [ "${networkId}" = "null" ]; then
            echo "Network not found"
            return
        fi
        curl -X PUT "${addToNetwork}?networkId=${networkId}" \
            -H "Authorization: Bearer ${jwt}"
        echo "Added ${fourCharId} to ${networkName}"
    done
}

addSiteToNetwork 00NA APREF
addSiteToNetwork 01NA APREF
addSiteToNetwork 02NA APREF
addSiteToNetwork 20NA APREF
addSiteToNetwork 21NA APREF
addSiteToNetwork A351 CAMPAIGN
addSiteToNetwork A368 CAMPAIGN
addSiteToNetwork ABRK APREF
addSiteToNetwork ACAD APREF
addSiteToNetwork ADE1 IGS APREF
addSiteToNetwork ADE2 IGS APREF
addSiteToNetwork ADEL APREF GPSNET
addSiteToNetwork AFHT APREF
addSiteToNetwork AFKB APREF
addSiteToNetwork AHVZ APREF
addSiteToNetwork AIRA IGS APREF
addSiteToNetwork AJAC IGS
addSiteToNetwork ALBH IGS
addSiteToNetwork ALBU APREF GPSNET
addSiteToNetwork ALBY APREF AUSCOPE
addSiteToNetwork ALGO IGS
addSiteToNetwork ALIC IGS APREF ARGN
addSiteToNetwork ALRT IGS
addSiteToNetwork AMC2 IGS
addSiteToNetwork ANDA APREF AUSCOPE ARGN
addSiteToNetwork ANGL APREF
addSiteToNetwork ANGS APREF GPSNET
addSiteToNetwork ANKR IGS
addSiteToNetwork ANNA APREF CORSNET-NSW
addSiteToNetwork ANTC IGS
addSiteToNetwork ANTW APREF GPSNET
addSiteToNetwork APOL APREF
addSiteToNetwork APSL APREF GPSNET
addSiteToNetwork ARAR APREF
addSiteToNetwork ARD2 APREF CORSNET-NSW
addSiteToNetwork ARDL APREF CORSNET-NSW
addSiteToNetwork ARMC APREF AUSCOPE
addSiteToNetwork ARMD APREF CORSNET-NSW
addSiteToNetwork ARRT APREF GPSNET
addSiteToNetwork ARTU IGS
addSiteToNetwork ARUB APREF AUSCOPE
addSiteToNetwork ASC1 IGS
addSiteToNetwork ASHF APREF CORSNET-NSW
addSiteToNetwork ASPA IGS
addSiteToNetwork AUCK IGS APREF POSITIONZ
addSiteToNetwork AUKT APREF GEONET
addSiteToNetwork BACC APREF
addSiteToNetwork BADG IGS
addSiteToNetwork BAIR APREF
addSiteToNetwork BAKE IGS
addSiteToNetwork BAKO IGS APREF
addSiteToNetwork BALA APREF AUSCOPE
addSiteToNetwork BALI APREF RTKNETWEST
addSiteToNetwork BALL APREF GPSNET
addSiteToNetwork BALM APREF GPSNET
addSiteToNetwork BALN APREF CORSNET-NSW
addSiteToNetwork BAN2 IGS
addSiteToNetwork BANK APREF CORSNET-NSW
addSiteToNetwork BARH IGS
addSiteToNetwork BARR APREF CORSNET-NSW
addSiteToNetwork BATH APREF CORSNET-NSW
addSiteToNetwork BBOO APREF AUSCOPE
addSiteToNetwork BCUS APREF GPSNET
addSiteToNetwork BDLE APREF AUSCOPE
addSiteToNetwork BDST APREF SUNPOZ
addSiteToNetwork BDVL APREF AUSCOPE
addSiteToNetwork BEE2 APREF SUNPOZ
addSiteToNetwork BEEC APREF AUSCOPE
addSiteToNetwork BEGA APREF CORSNET-NSW
addSiteToNetwork BENA APREF
addSiteToNetwork BEUA APREF GPSNET
addSiteToNetwork BEUL APREF
addSiteToNetwork BF38 APREF RTKNETWEST
addSiteToNetwork BHIL CAMPAIGN
addSiteToNetwork BHR2 IGS
addSiteToNetwork BHR3 IGS
addSiteToNetwork BHR4 IGS
addSiteToNetwork BIGG APREF GPSNET
addSiteToNetwork BILI IGS
addSiteToNetwork BING APREF AUSCOPE
addSiteToNetwork BINN APREF RTKNETWEST
addSiteToNetwork BIRC APREF GPSNET
addSiteToNetwork BJCT APREF CORSNET-NSW
addSiteToNetwork BJFS IGS APREF
addSiteToNetwork BJNM IGS APREF
addSiteToNetwork BKNL APREF AUSCOPE
addSiteToNetwork BLCK APREF CORSNET-NSW
addSiteToNetwork BLKT APREF CORSNET-NSW
addSiteToNetwork BLRN APREF CORSNET-NSW
addSiteToNetwork BLUF APREF POSITIONZ GEONET
addSiteToNetwork BMAN APREF AUSCOPE ARGN
addSiteToNetwork BNDC APREF GPSNET
addSiteToNetwork BNDY APREF ARGN
addSiteToNetwork BNLA APREF GPSNET
addSiteToNetwork BOGT IGS
addSiteToNetwork BOLA APREF
addSiteToNetwork BOLC APREF GPSNET
addSiteToNetwork BOMB APREF CORSNET-NSW
addSiteToNetwork BOOL APREF GPSNET
addSiteToNetwork BOOR APREF CORSNET-NSW
addSiteToNetwork BOR1 IGS
addSiteToNetwork BORA APREF CORSNET-NSW
addSiteToNetwork BORT APREF GPSNET
addSiteToNetwork BRAZ IGS
addSiteToNetwork BRBA APREF CORSNET-NSW
addSiteToNetwork BRDW APREF CORSNET-NSW
addSiteToNetwork BREW IGS
addSiteToNetwork BRFT IGS
addSiteToNetwork BRLA APREF AUSCOPE
addSiteToNetwork BRMU IGS
addSiteToNetwork BRO1 APREF AUSCOPE
addSiteToNetwork BROC APREF AUSCOPE
addSiteToNetwork BRST IGS
addSiteToNetwork BRUN IGS APREF
addSiteToNetwork BRUS IGS
addSiteToNetwork BRWN APREF CORSNET-NSW
addSiteToNetwork BUCH APREF GPSNET
addSiteToNetwork BUCU IGS
addSiteToNetwork BUE2 IGS
addSiteToNetwork BULA APREF AUSCOPE
addSiteToNetwork BUR1 APREF ARGN
addSiteToNetwork BUR2 APREF ARGN
addSiteToNetwork BURA APREF AUSCOPE
addSiteToNetwork BURK APREF CORSNET-NSW
addSiteToNetwork BUSS APREF RTKNETWEST
addSiteToNetwork BUXT APREF
addSiteToNetwork BZGN APREF
addSiteToNetwork CA07 APREF CAMPAIGN
addSiteToNetwork CA10 APREF CAMPAIGN
addSiteToNetwork CA19 APREF CAMPAIGN
addSiteToNetwork CAGL IGS
addSiteToNetwork CAGZ IGS
addSiteToNetwork CANR APREF GPSNET
addSiteToNetwork CARG APREF CORSNET-NSW
addSiteToNetwork CAS1 IGS APREF ARGN
addSiteToNetwork CAVL APREF
addSiteToNetwork CBAR APREF CORSNET-NSW
addSiteToNetwork CBLA APREF ARGN
addSiteToNetwork CBLE APREF CORSNET-NSW
addSiteToNetwork CBLT APREF SUNPOZ
addSiteToNetwork CBRA APREF GPSNET
addSiteToNetwork CCJ2 IGS APREF
addSiteToNetwork CCJM IGS APREF
addSiteToNetwork CEDU IGS APREF ARGN
addSiteToNetwork CESS APREF CORSNET-NSW
addSiteToNetwork CHAT IGS APREF
addSiteToNetwork CHIP APREF
addSiteToNetwork CHNG APREF
addSiteToNetwork CHPI IGS
addSiteToNetwork CHTI IGS APREF GEONET
addSiteToNetwork CHUM IGS APREF
addSiteToNetwork CHUR IGS
addSiteToNetwork CKIS IGS APREF SPRGN
addSiteToNetwork CKSV APREF
addSiteToNetwork CKWL APREF CORSNET-NSW
addSiteToNetwork CLAC APREF GPSNET
addSiteToNetwork CLAH APREF CORSNET-NSW
addSiteToNetwork CLBI APREF CORSNET-NSW
addSiteToNetwork CLBN APREF GPSNET
addSiteToNetwork CLEV APREF
addSiteToNetwork CLYT APREF GPSNET
addSiteToNetwork CMUM IGS
addSiteToNetwork CNBN APREF AUSCOPE
addSiteToNetwork CNDA APREF CORSNET-NSW
addSiteToNetwork CNDO APREF CORSNET-NSW
addSiteToNetwork CNMR IGS
addSiteToNetwork CNWD APREF CORSNET-NSW
addSiteToNetwork COAL APREF
addSiteToNetwork COBG APREF GPSNET
addSiteToNetwork COBH APREF CORSNET-NSW
addSiteToNetwork COBR APREF
addSiteToNetwork COCO IGS APREF ARGN
addSiteToNetwork COEN APREF AUSCOPE
addSiteToNetwork COFF APREF CORSNET-NSW
addSiteToNetwork COLE APREF CORSNET-NSW
addSiteToNetwork COLL APREF RTKNETWEST
addSiteToNetwork COMA APREF CORSNET-NSW
addSiteToNetwork CONZ IGS
addSiteToNetwork COOB APREF AUSCOPE ARGN
addSiteToNetwork COOL APREF AUSCOPE
addSiteToNetwork COPS APREF CORSNET-NSW
addSiteToNetwork CORD IGS
addSiteToNetwork CORM APREF POSITIONZ GEONET
addSiteToNetwork COYQ IGS
addSiteToNetwork CPNM IGS
addSiteToNetwork CRAN APREF GPSNET
addSiteToNetwork CRAO IGS
addSiteToNetwork CRDX APREF CORSNET-NSW
addSiteToNetwork CRES APREF
addSiteToNetwork CRO1 IGS
addSiteToNetwork CRSY APREF GPSNET
addSiteToNetwork CSNO APREF CORSNET-NSW
addSiteToNetwork CTMD APREF CORSNET-NSW
addSiteToNetwork CUND APREF RTKNETWEST
addSiteToNetwork CUSV IGS
addSiteToNetwork CUT0 IGS APREF
addSiteToNetwork CUUT IGS
addSiteToNetwork CWN2 APREF CORSNET-NSW
addSiteToNetwork CWRA APREF CORSNET-NSW
addSiteToNetwork DAE2 IGS
addSiteToNetwork DAEJ IGS
addSiteToNetwork DALB APREF SUNPOZ
addSiteToNetwork DARM APREF ARGN
addSiteToNetwork DARR IGS APREF ARGN
addSiteToNetwork DARW IGS APREF ARGN
addSiteToNetwork DAV1 IGS APREF ARGN
addSiteToNetwork DAVE APREF ARGN
addSiteToNetwork DAVR IGS APREF ARGN
addSiteToNetwork DBBO APREF CORSNET-NSW
addSiteToNetwork DEAR IGS
addSiteToNetwork DGAR IGS
addSiteToNetwork DKSN APREF CORSNET-NSW
addSiteToNetwork DLQN APREF CORSNET-NSW
addSiteToNetwork DLTV IGS
addSiteToNetwork DNVK APREF POSITIONZ GEONET
addSiteToNetwork DODA APREF AUSCOPE
addSiteToNetwork DORA APREF GPSNET
addSiteToNetwork DORR APREF CORSNET-NSW
addSiteToNetwork DPRT APREF
addSiteToNetwork DRAG IGS
addSiteToNetwork DRAO IGS
addSiteToNetwork DRGO APREF GPSNET
addSiteToNetwork DRHN APREF
addSiteToNetwork DSMG APREF
addSiteToNetwork DUBO IGS
addSiteToNetwork DUBR IGS
addSiteToNetwork DUM1 IGS
addSiteToNetwork DUND IGS APREF POSITIONZ GEONET
addSiteToNetwork DUNE APREF CORSNET-NSW
addSiteToNetwork DUNT APREF GEONET
addSiteToNetwork DWEL APREF
addSiteToNetwork DWHY APREF CORSNET-NSW
addSiteToNetwork DWNI APREF
addSiteToNetwork EBNK APREF GPSNET
addSiteToNetwork ECHU APREF GPSNET
addSiteToNetwork ECOR APREF CORSNET-NSW
addSiteToNetwork EDEN APREF CORSNET-NSW
addSiteToNetwork EDSV APREF AUSCOPE
addSiteToNetwork EIL3 IGS
addSiteToNetwork EIL4 IGS
addSiteToNetwork ELLI APREF
addSiteToNetwork EPRT IGS
addSiteToNetwork EPSM APREF GPSNET
addSiteToNetwork ERDN APREF
addSiteToNetwork ERMG APREF AUSCOPE
addSiteToNetwork ESPA APREF AUSCOPE
addSiteToNetwork ETAD APREF
addSiteToNetwork ETDD APREF
addSiteToNetwork ETJI APREF
addSiteToNetwork EURA APREF GPSNET
addSiteToNetwork EURO APREF
addSiteToNetwork EUSM IGS APREF
addSiteToNetwork EXMT APREF AUSCOPE
addSiteToNetwork FAIR IGS
addSiteToNetwork FALE IGS APREF
addSiteToNetwork FLGP APREF CORSNET-NSW
addSiteToNetwork FLIN IGS
addSiteToNetwork FLND APREF AUSCOPE
addSiteToNetwork FOMO APREF
addSiteToNetwork FORB APREF CORSNET-NSW
addSiteToNetwork FORS APREF CORSNET-NSW
addSiteToNetwork FRDN IGS
addSiteToNetwork FROY APREF AUSCOPE
addSiteToNetwork FTDN APREF CORSNET-NSW
addSiteToNetwork GABO APREF AUSCOPE
addSiteToNetwork GASC APREF AUSCOPE
addSiteToNetwork GATT APREF SUNPOZ
addSiteToNetwork GEEL APREF
addSiteToNetwork GEES APREF GPSNET
addSiteToNetwork GELA APREF GPSNET
addSiteToNetwork GERR APREF CORSNET-NSW
addSiteToNetwork GETI APREF
addSiteToNetwork GFEL APREF CORSNET-NSW
addSiteToNetwork GFTH APREF CORSNET-NSW
addSiteToNetwork GFTN APREF CORSNET-NSW
addSiteToNetwork GGTN APREF AUSCOPE
addSiteToNetwork GILG APREF CORSNET-NSW
addSiteToNetwork GISB APREF POSITIONZ GEONET
addSiteToNetwork GLB2 APREF CORSNET-NSW
addSiteToNetwork GLBN APREF CORSNET-NSW
addSiteToNetwork GLDB APREF POSITIONZ GEONET
addSiteToNetwork GLDN APREF GPSNET
addSiteToNetwork GLEN APREF GPSNET
addSiteToNetwork GLIN APREF CORSNET-NSW
addSiteToNetwork GLPS IGS
addSiteToNetwork GLSV IGS
addSiteToNetwork GMAS IGS
addSiteToNetwork GMSD IGS APREF
addSiteToNetwork GNGN APREF CORSNET-NSW
addSiteToNetwork GNOA APREF GPSNET
addSiteToNetwork GODE IGS
addSiteToNetwork GOLD IGS
addSiteToNetwork GONG APREF CORSNET-NSW
addSiteToNetwork GOOL APREF CORSNET-NSW
addSiteToNetwork GORO APREF GPSNET
addSiteToNetwork GOUG IGS
addSiteToNetwork GRAS IGS
addSiteToNetwork GRAZ IGS
addSiteToNetwork GRIM CAMPAIGN
addSiteToNetwork GROT AUSCOPE
addSiteToNetwork GUAM IGS APREF
addSiteToNetwork GUAO IGS APREF
addSiteToNetwork GUAT IGS
addSiteToNetwork GUNN APREF CORSNET-NSW
addSiteToNetwork GURL APREF CORSNET-NSW
addSiteToNetwork GUUG IGS
addSiteToNetwork GWAB APREF CORSNET-NSW
addSiteToNetwork HAAS APREF POSITIONZ GEONET
addSiteToNetwork HAMI APREF
addSiteToNetwork HAMO APREF GPSNET
addSiteToNetwork HAMT APREF GEONET
addSiteToNetwork HARB IGS
addSiteToNetwork HAST APREF POSITIONZ GEONET
addSiteToNetwork HATT APREF GPSNET
addSiteToNetwork HAY1 APREF CORSNET-NSW
addSiteToNetwork HERN APREF AUSCOPE
addSiteToNetwork HERS IGS
addSiteToNetwork HERT IGS
addSiteToNetwork HIKB APREF POSITIONZ GEONET
addSiteToNetwork HIL1 APREF ARGN
addSiteToNetwork HILL APREF CORSNET-NSW
addSiteToNetwork HILO IGS
addSiteToNetwork HILR APREF
addSiteToNetwork HKCL APREF
addSiteToNetwork HKFN APREF
addSiteToNetwork HKKS APREF
addSiteToNetwork HKKT APREF
addSiteToNetwork HKLM APREF
addSiteToNetwork HKLT APREF
addSiteToNetwork HKMW APREF
addSiteToNetwork HKNP APREF
addSiteToNetwork HKOH APREF
addSiteToNetwork HKPC APREF
addSiteToNetwork HKSC APREF
addSiteToNetwork HKSL IGS APREF
addSiteToNetwork HKSS APREF
addSiteToNetwork HKST APREF
addSiteToNetwork HKTK APREF
addSiteToNetwork HKWS IGS APREF
addSiteToNetwork HLBK APREF CORSNET-NSW
addSiteToNetwork HLFX IGS
addSiteToNetwork HMLT APREF POSITIONZ
addSiteToNetwork HNIS APREF AUSCOPE
addSiteToNetwork HNLC IGS
addSiteToNetwork HNSB APREF CORSNET-NSW
addSiteToNetwork HNUS IGS
addSiteToNetwork HOB2 IGS APREF ARGN
addSiteToNetwork HOFN IGS
addSiteToNetwork HOKI APREF POSITIONZ GEONET
addSiteToNetwork HOLB IGS
addSiteToNetwork HOLM IGS
addSiteToNetwork HOTH APREF GPSNET
addSiteToNetwork HRAO IGS
addSiteToNetwork HRM1 IGS
addSiteToNetwork HRSM APREF GPSNET
addSiteToNetwork HSHM APREF
addSiteToNetwork HUGH APREF AUSCOPE
addSiteToNetwork HYDE IGS
addSiteToNetwork HYDN APREF AUSCOPE
addSiteToNetwork IHOE APREF AUSCOPE
addSiteToNetwork IISC IGS
addSiteToNetwork INVK IGS
addSiteToNetwork INVL APREF CORSNET-NSW
addSiteToNetwork IPS2 APREF
addSiteToNetwork IPSR APREF
addSiteToNetwork IRKJ IGS
addSiteToNetwork IRKT IGS
addSiteToNetwork IRYM APREF GPSNET
addSiteToNetwork ISBA IGS APREF
addSiteToNetwork ISBS APREF
addSiteToNetwork ISER APREF
addSiteToNetwork ISKU APREF
addSiteToNetwork ISNA APREF
addSiteToNetwork ISPA IGS
addSiteToNetwork ISSD APREF
addSiteToNetwork ISTA IGS
addSiteToNetwork JAB1 IGS APREF ARGN
addSiteToNetwork JAB2 APREF ARGN
addSiteToNetwork JCTW IGS
addSiteToNetwork JERI APREF CORSNET-NSW
addSiteToNetwork JERV APREF AUSCOPE
addSiteToNetwork JLCK APREF AUSCOPE
addSiteToNetwork JNAV IGS
addSiteToNetwork JOG2 IGS APREF
addSiteToNetwork JOZ2 IGS
addSiteToNetwork JOZE IGS
addSiteToNetwork JPLM IGS
addSiteToNetwork JPRE IGS
addSiteToNetwork JUML APREF
addSiteToNetwork JUNA APREF
addSiteToNetwork KAIK APREF POSITIONZ GEONET
addSiteToNetwork KALG APREF AUSCOPE
addSiteToNetwork KARR IGS APREF ARGN
addSiteToNetwork KAT1 IGS APREF ARGN
addSiteToNetwork KAT2 APREF ARGN
addSiteToNetwork KDAL APREF RTKNETWEST
addSiteToNetwork KELN APREF AUSCOPE
addSiteToNetwork KELY IGS
addSiteToNetwork KEPK APREF GPSNET
addSiteToNetwork KERG IGS APREF
addSiteToNetwork KGIS APREF AUSCOPE
addSiteToNetwork KGNI IGS APREF
addSiteToNetwork KHAJ IGS
addSiteToNetwork KILK APREF AUSCOPE
addSiteToNetwork KILM APREF GPSNET
addSiteToNetwork KIRI IGS APREF SPRGN
addSiteToNetwork KIRR APREF CORSNET-NSW
addSiteToNetwork KIRU IGS
addSiteToNetwork KIT3 IGS
addSiteToNetwork KMAN APREF AUSCOPE
addSiteToNetwork KMNM APREF
addSiteToNetwork KOK5 APREF
addSiteToNetwork KOKB IGS
addSiteToNetwork KOSG IGS
addSiteToNetwork KOUC IGS
addSiteToNetwork KOUR IGS
addSiteToNetwork KRNG APREF GPSNET
addSiteToNetwork KSTU IGS
addSiteToNetwork KTIA APREF POSITIONZ GEONET
addSiteToNetwork KTMB APREF CORSNET-NSW
addSiteToNetwork KTON APREF GPSNET
addSiteToNetwork KUAL APREF
addSiteToNetwork KULW APREF CORSNET-NSW
addSiteToNetwork KUNM IGS APREF
addSiteToNetwork KUNU APREF AUSCOPE
addSiteToNetwork KWJ1 IGS APREF
addSiteToNetwork KYNT APREF
addSiteToNetwork LAE1 IGS APREF
addSiteToNetwork LALB APREF GPSNET
addSiteToNetwork LAMB APREF AUSCOPE ARGN
addSiteToNetwork LARR APREF AUSCOPE
addSiteToNetwork LAUT IGS APREF SPRGN
addSiteToNetwork LCK3 IGS
addSiteToNetwork LCK4 IGS
addSiteToNetwork LCLA APREF GPSNET
addSiteToNetwork LDHI APREF
addSiteToNetwork LEXA APREF POSITIONZ GEONET
addSiteToNetwork LGOW APREF CORSNET-NSW
addSiteToNetwork LGT1 APREF
addSiteToNetwork LHAS IGS APREF
addSiteToNetwork LHAZ IGS APREF
addSiteToNetwork LIAW APREF AUSCOPE
addSiteToNetwork LILY APREF
addSiteToNetwork LIPO APREF GPSNET
addSiteToNetwork LIRI APREF CORSNET-NSW
addSiteToNetwork LKHT APREF CORSNET-NSW
addSiteToNetwork LKTA APREF POSITIONZ GEONET
addSiteToNetwork LKYA APREF AUSCOPE
addSiteToNetwork LONA APREF AUSCOPE
addSiteToNetwork LORA APREF AUSCOPE
addSiteToNetwork LORD APREF ARGN
addSiteToNetwork LOTH APREF CORSNET-NSW
addSiteToNetwork LPGS IGS
addSiteToNetwork LROC IGS
addSiteToNetwork LSB0 APREF
addSiteToNetwork LURA APREF AUSCOPE
addSiteToNetwork LYTT APREF GEONET
addSiteToNetwork MAC1 IGS APREF ARGN
addSiteToNetwork MACK APREF CORSNET-NSW
addSiteToNetwork MADR IGS
addSiteToNetwork MAFF APREF GPSNET
addSiteToNetwork MAG0 IGS
addSiteToNetwork MAHO APREF POSITIONZ GEONET
addSiteToNetwork MAIN APREF AUSCOPE
addSiteToNetwork MAJU IGS APREF SPRGN
addSiteToNetwork MALD IGS
addSiteToNetwork MALI IGS
addSiteToNetwork MANA IGS
addSiteToNetwork MANY APREF GPSNET
addSiteToNetwork MAR6 IGS
addSiteToNetwork MARS IGS
addSiteToNetwork MARY APREF GPSNET
addSiteToNetwork MAS1 IGS
addSiteToNetwork MAST APREF POSITIONZ
addSiteToNetwork MAT1 IGS
addSiteToNetwork MATE IGS
addSiteToNetwork MAUI IGS
addSiteToNetwork MAVL APREF POSITIONZ GEONET
addSiteToNetwork MAW1 IGS APREF ARGN
addSiteToNetwork MBAR IGS
addSiteToNetwork MCHL IGS APREF ARGN
addSiteToNetwork MCIL IGS APREF
addSiteToNetwork MCM4 IGS
addSiteToNetwork MDAH APREF RTKNETWEST
addSiteToNetwork MDO1 IGS
addSiteToNetwork MDVJ IGS
addSiteToNetwork MDVO IGS
addSiteToNetwork MEDI IGS
addSiteToNetwork MEDO APREF AUSCOPE
addSiteToNetwork MENA APREF CORSNET-NSW
addSiteToNetwork MENO APREF GPSNET
addSiteToNetwork METH APREF POSITIONZ GEONET
addSiteToNetwork METS IGS
addSiteToNetwork METZ IGS
addSiteToNetwork MFKG IGS
addSiteToNetwork MGRV APREF CORSNET-NSW
addSiteToNetwork MIDL APREF RTKNETWEST
addSiteToNetwork MIMI APREF GPSNET
addSiteToNetwork MITT APREF GPSNET
addSiteToNetwork MIZU IGS APREF
addSiteToNetwork MKEA IGS
addSiteToNetwork MLAK APREF GPSNET
addSiteToNetwork MLO1 APREF
addSiteToNetwork MNDE APREF CORSNET-NSW
addSiteToNetwork MNGO APREF AUSCOPE
addSiteToNetwork MNPK APREF CORSNET-NSW
addSiteToNetwork MNSF APREF GPSNET
addSiteToNetwork MOBS IGS APREF ARGN
addSiteToNetwork MONP IGS
addSiteToNetwork MOOR APREF GPSNET
addSiteToNetwork MORP IGS
addSiteToNetwork MOUL APREF CORSNET-NSW
addSiteToNetwork MQZG IGS APREF POSITIONZ GEONET
addSiteToNetwork MRBA APREF AUSCOPE
addSiteToNetwork MREE APREF CORSNET-NSW
addSiteToNetwork MRL1 IGS APREF
addSiteToNetwork MRL2 IGS APREF
addSiteToNetwork MRNO APREF GPSNET
addSiteToNetwork MRNT APREF GPSNET
addSiteToNetwork MRO1 IGS APREF ARGN
addSiteToNetwork MRWA APREF CORSNET-NSW
addSiteToNetwork MSHN APREF
addSiteToNetwork MSVL APREF CORSNET-NSW
addSiteToNetwork MTBU APREF GPSNET
addSiteToNetwork MTCV APREF AUSCOPE
addSiteToNetwork MTDN APREF AUSCOPE
addSiteToNetwork MTEM APREF AUSCOPE
addSiteToNetwork MTHR APREF CORSNET-NSW
addSiteToNetwork MTIS APREF AUSCOPE
addSiteToNetwork MTJO APREF POSITIONZ GEONET
addSiteToNetwork MTKA IGS APREF
addSiteToNetwork MTLD APREF CORSNET-NSW
addSiteToNetwork MTMA APREF AUSCOPE
addSiteToNetwork MTV1 IGS
addSiteToNetwork MTV2 IGS
addSiteToNetwork MUDG APREF CORSNET-NSW
addSiteToNetwork MULG APREF AUSCOPE ARGN
addSiteToNetwork MURR APREF GPSNET
addSiteToNetwork MVIL APREF GPSNET
addSiteToNetwork MWAL APREF CORSNET-NSW
addSiteToNetwork MYRT APREF GPSNET
addSiteToNetwork NAIN IGS
addSiteToNetwork NANO IGS
addSiteToNetwork NAUR IGS APREF SPRGN
addSiteToNetwork NBRI APREF CORSNET-NSW
addSiteToNetwork NBRK APREF AUSCOPE
addSiteToNetwork NCKU IGS
addSiteToNetwork NCLF APREF AUSCOPE
addSiteToNetwork NDRA APREF CORSNET-NSW
addSiteToNetwork NEBO APREF AUSCOPE
addSiteToNetwork NELN APREF GPSNET
addSiteToNetwork NEWE APREF CORSNET-NSW
addSiteToNetwork NEWH APREF GPSNET
addSiteToNetwork NGAN APREF CORSNET-NSW
addSiteToNetwork NHIL APREF AUSCOPE
addSiteToNetwork NICO IGS
addSiteToNetwork NIUM IGS APREF SPRGN
addSiteToNetwork NIUT APREF SPRGN
addSiteToNetwork NKLG IGS
addSiteToNetwork NLIB IGS
addSiteToNetwork NLSN APREF POSITIONZ GEONET
addSiteToNetwork NMB2 APREF
addSiteToNetwork NMBN APREF CORSNET-NSW
addSiteToNetwork NML1 APREF
addSiteToNetwork NMTN APREF AUSCOPE
addSiteToNetwork NNOR IGS APREF
addSiteToNetwork NORF APREF ARGN
addSiteToNetwork NORS APREF AUSCOPE
addSiteToNetwork NOT1 IGS
addSiteToNetwork NOUM IGS
addSiteToNetwork NOVM IGS
addSiteToNetwork NOWE APREF CORSNET-NSW
addSiteToNetwork NPLY APREF POSITIONZ GEONET
addSiteToNetwork NRC1 IGS
addSiteToNetwork NRIL IGS
addSiteToNetwork NRMD IGS
addSiteToNetwork NRMN APREF CORSNET-NSW
addSiteToNetwork NSSP IGS
addSiteToNetwork NSTA APREF AUSCOPE
addSiteToNetwork NTJN APREF AUSCOPE
addSiteToNetwork NTUS IGS
addSiteToNetwork NVSK IGS
addSiteToNetwork NW00 APREF
addSiteToNetwork NWCS APREF CORSNET-NSW
addSiteToNetwork NWRA APREF CORSNET-NSW
addSiteToNetwork NYA1 IGS
addSiteToNetwork NYAL IGS
addSiteToNetwork NYMA APREF CORSNET-NSW
addSiteToNetwork OAK1 IGS
addSiteToNetwork OAK2 IGS
addSiteToNetwork OBRN APREF CORSNET-NSW
addSiteToNetwork OHI2 IGS
addSiteToNetwork OHI3 IGS
addSiteToNetwork OHIG IGS
addSiteToNetwork OMEO APREF GPSNET
addSiteToNetwork ONSA IGS
addSiteToNetwork ORBO APREF GPSNET
addSiteToNetwork ORNG APREF CORSNET-NSW
addSiteToNetwork OSN1 IGS
addSiteToNetwork OSN3 IGS
addSiteToNetwork OSN4 IGS
addSiteToNetwork OUS2 IGS APREF GEONET
addSiteToNetwork OUSD APREF POSITIONZ GEONET
addSiteToNetwork OVAL APREF CORSNET-NSW
addSiteToNetwork OWNG IGS APREF
addSiteToNetwork PACH APREF GPSNET
addSiteToNetwork PAH6 APREF
addSiteToNetwork PALM IGS
addSiteToNetwork PARC IGS
addSiteToNetwork PARK IGS APREF ARGN
addSiteToNetwork PATC APREF
addSiteToNetwork PBOT APREF CORSNET-NSW
addSiteToNetwork PBRI IGS
addSiteToNetwork PCTN APREF CORSNET-NSW
addSiteToNetwork PDEL IGS
addSiteToNetwork PERI APREF CORSNET-NSW
addSiteToNetwork PERT IGS APREF ARGN
addSiteToNetwork PETP IGS
addSiteToNetwork PETS IGS
addSiteToNetwork PGEN IGS APREF
addSiteToNetwork PIAN APREF GPSNET
addSiteToNetwork PIE1 IGS
addSiteToNetwork PIMO IGS APREF
addSiteToNetwork PKVL APREF GPSNET
addSiteToNetwork PMAC APREF CORSNET-NSW
addSiteToNetwork PNGM IGS APREF SPRGN
addSiteToNetwork POCA APREF GPSNET
addSiteToNetwork POHN IGS APREF SPRGN
addSiteToNetwork POL2 IGS
addSiteToNetwork POLV IGS
addSiteToNetwork POON APREF CORSNET-NSW
addSiteToNetwork POTS IGS
addSiteToNetwork PPPC IGS APREF
addSiteToNetwork PRCE APREF CORSNET-NSW
addSiteToNetwork PRDS IGS
addSiteToNetwork PRE1 IGS
addSiteToNetwork PRE3 IGS
addSiteToNetwork PRE4 IGS
addSiteToNetwork PRKS APREF CORSNET-NSW
addSiteToNetwork PRTF APREF GPSNET
addSiteToNetwork PSPJ APREF
addSiteToNetwork PTAG IGS APREF
addSiteToNetwork PTGG IGS APREF
addSiteToNetwork PTHL APREF AUSCOPE
addSiteToNetwork PTKL APREF AUSCOPE
addSiteToNetwork PTLD APREF AUSCOPE
addSiteToNetwork PTSV APREF AUSCOPE ARGN
addSiteToNetwork PTVL IGS APREF SPRGN
addSiteToNetwork PUTY APREF CORSNET-NSW
addSiteToNetwork PYGR APREF POSITIONZ GEONET
addSiteToNetwork QAQ1 IGS
addSiteToNetwork QCLF APREF GPSNET
addSiteToNetwork QIKI IGS
addSiteToNetwork QUAM APREF CORSNET-NSW
addSiteToNetwork QUI2 IGS
addSiteToNetwork QUI3 IGS
addSiteToNetwork QUI4 IGS
addSiteToNetwork QUIN IGS
addSiteToNetwork RABT IGS
addSiteToNetwork RAMO IGS
addSiteToNetwork RAND APREF CORSNET-NSW
addSiteToNetwork RANK APREF CORSNET-NSW
addSiteToNetwork RAVN APREF AUSCOPE
addSiteToNetwork RBAY IGS
addSiteToNetwork RBVL APREF GPSNET
addSiteToNetwork RESO IGS
addSiteToNetwork REUN IGS
addSiteToNetwork REYK IGS
addSiteToNetwork RGLN APREF CORSNET-NSW
addSiteToNetwork RHPT APREF ARGN
addSiteToNetwork RIGA IGS
addSiteToNetwork RIOG IGS
addSiteToNetwork RIVN APREF AUSCOPE
addSiteToNetwork RKLD APREF AUSCOPE
addSiteToNetwork RNBO APREF GPSNET
addSiteToNetwork RNBW APREF
addSiteToNetwork RNSP APREF AUSCOPE
addSiteToNetwork ROBI APREF SUNPOZ
addSiteToNetwork ROTT APREF RTKNETWEST
addSiteToNetwork RSBY APREF AUSCOPE
addSiteToNetwork RUTH APREF GPSNET
addSiteToNetwork RUUS APREF CORSNET-NSW
addSiteToNetwork RVO_ APREF
addSiteToNetwork RYLS APREF CORSNET-NSW
addSiteToNetwork SA45 APREF AUSCOPE ARGN
addSiteToNetwork SAMO IGS APREF SPRGN
addSiteToNetwork SANT IGS
addSiteToNetwork SASS IGS
addSiteToNetwork SBOK IGS
addSiteToNetwork SCH2 IGS
addSiteToNetwork SCON APREF CORSNET-NSW
addSiteToNetwork SCOR IGS
addSiteToNetwork SCTB IGS APREF GEONET
addSiteToNetwork SCUB IGS
addSiteToNetwork SEAL APREF GPSNET
addSiteToNetwork SEJN IGS
addSiteToNetwork SELE IGS APREF
addSiteToNetwork SEMR APREF GPSNET
addSiteToNetwork SEY1 IGS
addSiteToNetwork SFER IGS
addSiteToNetwork SHAO IGS APREF
addSiteToNetwork SHRZ APREF
addSiteToNetwork SIMO IGS
addSiteToNetwork SKIP APREF GPSNET
addSiteToNetwork SNGO APREF CORSNET-NSW
addSiteToNetwork SOFI IGS
addSiteToNetwork SOLO IGS APREF SPRGN
addSiteToNetwork SPBY APREF AUSCOPE
addSiteToNetwork SPPT APREF CORSNET-NSW
addSiteToNetwork SPWD APREF CORSNET-NSW
addSiteToNetwork SRVC APREF GPSNET
addSiteToNetwork SSIA IGS
addSiteToNetwork STAR APREF GPSNET
addSiteToNetwork STHG APREF AUSCOPE
addSiteToNetwork STJO IGS
addSiteToNetwork STNY APREF AUSCOPE
addSiteToNetwork STR1 IGS APREF
addSiteToNetwork STR2 IGS APREF ARGN
addSiteToNetwork STR3 APREF ARGN
addSiteToNetwork STRH APREF GPSNET
addSiteToNetwork SUTH IGS
addSiteToNetwork SUTM IGS
addSiteToNetwork SUVA IGS APREF
addSiteToNetwork SUWN IGS
addSiteToNetwork SW01 CAMPAIGN
addSiteToNetwork SW02 CAMPAIGN
addSiteToNetwork SW03 CAMPAIGN
addSiteToNetwork SW04 CAMPAIGN
addSiteToNetwork SWNH APREF GPSNET
addSiteToNetwork SYDN IGS APREF ARGN
addSiteToNetwork SYM1 APREF ARGN
addSiteToNetwork SYOG IGS
addSiteToNetwork T430 APREF
addSiteToNetwork TAH1 IGS APREF
addSiteToNetwork TAKL APREF
addSiteToNetwork TAMW APREF CORSNET-NSW
addSiteToNetwork TANA IGS
addSiteToNetwork TARE APREF CORSNET-NSW
addSiteToNetwork TATU APREF GPSNET
addSiteToNetwork TAUP APREF POSITIONZ GEONET
addSiteToNetwork TBOB APREF AUSCOPE
addSiteToNetwork TCMS IGS APREF
addSiteToNetwork TDOU IGS
addSiteToNetwork TEHN IGS APREF
addSiteToNetwork TELO APREF GPSNET
addSiteToNetwork TGCV IGS
addSiteToNetwork THEV APREF AUSCOPE ARGN
addSiteToNetwork THOM APREF GPSNET
addSiteToNetwork THTI IGS APREF
addSiteToNetwork THU3 IGS
addSiteToNetwork TID1 IGS APREF ARGN
addSiteToNetwork TIDB IGS APREF
addSiteToNetwork TITG APREF ARGN
addSiteToNetwork TIXI IGS
addSiteToNetwork TLPA APREF CORSNET-NSW
addSiteToNetwork TLSE IGS
addSiteToNetwork TMBA APREF CORSNET-NSW
addSiteToNetwork TMBO APREF AUSCOPE
addSiteToNetwork TMRA APREF CORSNET-NSW
addSiteToNetwork TMUT APREF CORSNET-NSW
addSiteToNetwork TNML IGS APREF
addSiteToNetwork TNTR APREF CORSNET-NSW
addSiteToNetwork TOKA APREF
addSiteToNetwork TOMP APREF AUSCOPE
addSiteToNetwork TONG IGS APREF SPRGN
addSiteToNetwork TOOG APREF SUNPOZ
addSiteToNetwork TOOW APREF AUSCOPE
addSiteToNetwork TORK APREF RTKNETWEST
addSiteToNetwork TOTT APREF CORSNET-NSW
addSiteToNetwork TOW2 IGS APREF ARGN
addSiteToNetwork TRAB IGS
addSiteToNetwork TRNA APREF
addSiteToNetwork TRNG APREF POSITIONZ GEONET
addSiteToNetwork TRO1 IGS
addSiteToNetwork TROM IGS
addSiteToNetwork TSKB IGS APREF
addSiteToNetwork TUAS APREF
addSiteToNetwork TUKT IGS
addSiteToNetwork TULL APREF CORSNET-NSW
addSiteToNetwork TURO APREF AUSCOPE
addSiteToNetwork TUVA IGS APREF SPRGN
addSiteToNetwork TWNT APREF
addSiteToNetwork TWTF IGS
addSiteToNetwork UB01 APREF
addSiteToNetwork UCLA APREF AUSCOPE
addSiteToNetwork UCLU IGS
addSiteToNetwork UKUR APREF
addSiteToNetwork ULAB IGS APREF
addSiteToNetwork ULDI IGS
addSiteToNetwork ULLA APREF CORSNET-NSW
addSiteToNetwork UMAS APREF
addSiteToNetwork UMSS APREF
addSiteToNetwork UMTA IGS
addSiteToNetwork UNDE APREF GPSNET
addSiteToNetwork UNSA IGS
addSiteToNetwork UNSW APREF CORSNET-NSW
addSiteToNetwork UPO5 APREF
addSiteToNetwork URUM IGS APREF
addSiteToNetwork USAG APREF
addSiteToNetwork USN3 IGS
addSiteToNetwork USNO IGS
addSiteToNetwork USUD IGS APREF
addSiteToNetwork UZHL IGS
addSiteToNetwork VALP IGS
addSiteToNetwork VANU APREF SPRGN
addSiteToNetwork VESL IGS
addSiteToNetwork VGMT APREF POSITIONZ GEONET
addSiteToNetwork VILL IGS
addSiteToNetwork VLWD APREF CORSNET-NSW
addSiteToNetwork VNDP IGS
addSiteToNetwork WAGN APREF AUSCOPE
addSiteToNetwork WAIG APREF
addSiteToNetwork WAIM APREF POSITIONZ GEONET
addSiteToNetwork WAKL APREF CORSNET-NSW
addSiteToNetwork WALH APREF AUSCOPE
addSiteToNetwork WALW APREF GPSNET
addSiteToNetwork WANG APREF POSITIONZ GEONET
addSiteToNetwork WARA APREF ARGN
addSiteToNetwork WARI APREF CORSNET-NSW
addSiteToNetwork WARK IGS APREF POSITIONZ GEONET
addSiteToNetwork WARN IGS
addSiteToNetwork WARW APREF SUNPOZ
addSiteToNetwork WBEE APREF GPSNET
addSiteToNetwork WDBG APREF CORSNET-NSW
addSiteToNetwork WDC5 IGS
addSiteToNetwork WDC6 IGS
addSiteToNetwork WEDD APREF GPSNET
addSiteToNetwork WEEM APREF CORSNET-NSW
addSiteToNetwork WEIP APREF
addSiteToNetwork WEL1 IGS APREF GEONET
addSiteToNetwork WEND APREF GPSNET
addSiteToNetwork WES2 IGS
addSiteToNetwork WEST APREF POSITIONZ GEONET
addSiteToNetwork WFAL APREF CORSNET-NSW
addSiteToNetwork WGGA APREF CORSNET-NSW
addSiteToNetwork WGTN IGS APREF POSITIONZ GEONET
addSiteToNetwork WGTT APREF POSITIONZ GEONET
addSiteToNetwork WHIT IGS APREF
addSiteToNetwork WHIY APREF RTKNETWEST
addSiteToNetwork WHKT APREF POSITIONZ GEONET
addSiteToNetwork WHNG APREF POSITIONZ GEONET
addSiteToNetwork WHTL APREF
addSiteToNetwork WILL IGS
addSiteToNetwork WILU APREF AUSCOPE
addSiteToNetwork WIND IGS
addSiteToNetwork WLAL APREF AUSCOPE
addSiteToNetwork WLCA APREF CORSNET-NSW
addSiteToNetwork WLGT APREF CORSNET-NSW
addSiteToNetwork WLWN APREF CORSNET-NSW
addSiteToNetwork WMGA APREF AUSCOPE
addSiteToNetwork WOOL APREF
addSiteToNetwork WOOR APREF
addSiteToNetwork WORI APREF GPSNET
addSiteToNetwork WOTG APREF GPSNET
addSiteToNetwork WRPA APREF POSITIONZ GEONET
addSiteToNetwork WRRN APREF CORSNET-NSW
addSiteToNetwork WSRT IGS
addSiteToNetwork WTCF APREF CORSNET-NSW
addSiteToNetwork WTON APREF CORSNET-NSW
addSiteToNetwork WTZR IGS
addSiteToNetwork WUHN IGS APREF
addSiteToNetwork WWLG APREF AUSCOPE
addSiteToNetwork WYCH APREF GPSNET
addSiteToNetwork WYNG APREF CORSNET-NSW
addSiteToNetwork XIAN IGS APREF
addSiteToNetwork XMIS IGS APREF ARGN
addSiteToNetwork YAKT IGS
addSiteToNetwork YALL APREF GPSNET
addSiteToNetwork YANA APREF
addSiteToNetwork YANK APREF GPSNET
addSiteToNetwork YAR2 IGS APREF ARGN
addSiteToNetwork YAR3 IGS APREF ARGN
addSiteToNetwork YARO APREF CORSNET-NSW
addSiteToNetwork YARR IGS APREF ARGN
addSiteToNetwork YARS APREF GPSNET
addSiteToNetwork YASS APREF CORSNET-NSW
addSiteToNetwork YEBE IGS
addSiteToNetwork YEEL APREF AUSCOPE ARGN
addSiteToNetwork YELL IGS
addSiteToNetwork YELO APREF AUSCOPE
addSiteToNetwork YIBL IGS
addSiteToNetwork YIEL APREF GPSNET
addSiteToNetwork YMBA APREF CORSNET-NSW
addSiteToNetwork YNKI APREF AUSCOPE
addSiteToNetwork YONS IGS
addSiteToNetwork YRRM APREF GPSNET
addSiteToNetwork YSSK IGS
addSiteToNetwork YULA APREF AUSCOPE
addSiteToNetwork YUNG APREF CORSNET-NSW
addSiteToNetwork ZABL APREF
addSiteToNetwork ZAMB IGS
addSiteToNetwork ZECK IGS
addSiteToNetwork ZHN1 APREF
addSiteToNetwork ZIMJ IGS
addSiteToNetwork ZIMM IGS
