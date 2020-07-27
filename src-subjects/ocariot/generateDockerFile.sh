#!/usr/bin/env bash

success_message(){
	echo "Genenation of yml file with success."
}

YML_FILE_BASE=$1
YML_FILE_GENERATED="$(echo ${YML_FILE_BASE} | sed 's![^/]*$!!')docker-generated.yml"
SERVICES=${@:2}

if [ "$2" = "all" ]; then
	cat ${YML_FILE_BASE} > ${YML_FILE_GENERATED}
	success_message
	exit 0
fi

for SERVICE in ${@:2};
do
	if [ -z "$(cat ${YML_FILE_BASE} | yq r - "services.${SERVICE}")" ]; then
		echo "Service ${SERVICE} not found int ${YML_FILE_BASE}"
		exit 1
	fi
	YML_SERVICES="${YML_SERVICES}\n""$(cat ${YML_FILE_BASE} | yq r - "services.${SERVICE}" | yq p - "${SERVICE}")"
	if [ "$(echo ${SERVICE} | grep -e "mongo\|redis\|mysql")" ]; then
		YML_VOLUMES="${YML_VOLUMES}\n""$(cat ${YML_FILE_BASE} | yq r - "volumes.${SERVICE}-data" | yq p - "${SERVICE}-data")"
	fi
done

YML_SERVICES=$(echo -e "${YML_SERVICES}" | tail -n +2)
YML_VOLUMES=$(echo -e "${YML_VOLUMES}" | tail -n +2)
cat ${YML_FILE_BASE} \
	| yq d - "services.*" \
	| yq w - "services" "${YML_SERVICES}" \
	| yq d - "volumes.*" \
	| yq w - "volumes" "${YML_VOLUMES}" \
	| sed 's@|-@@g' \
	| yq d - services.*.depends_on > ${YML_FILE_GENERATED}

if [ $? -ne 0 ]; then
	echo "Problems in genenation of yml file."
	exit 1
fi

success_message

