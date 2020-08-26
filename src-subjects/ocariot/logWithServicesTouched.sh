#!/usr/bin/env bash

YML_FILE_BASE="./routes.yml"
MICROSERVICES=$(cat ${YML_FILE_BASE} \
	| yq r - "microservices" \
	| grep -vE '^ |mongo|redis|mysql' \
	| sed 's@:.*@@g')
TIME=$(date +'%Y-%m-%dT%H:%M:%S')
OUT=$(npm run test:single --prefix $2 -- --grep $1 test/**/**/*.spec.ts 2>&1)
CODE=$?

OUT=$(echo "${OUT}" | grep -E "^ ")
FAIL_NUMBER_LINE=$(echo "${OUT}" | grep -nE "failing" | awk '{print $1}' | sed 's@:@@g')
if [ "${FAIL_NUMBER_LINE}" ]; then
	echo "${OUT}" | tail -n +$(((1 + ${FAIL_NUMBER_LINE})))
fi

if [ -z "$3" ]; then
	exit ${CODE}
fi

ROUTES=$(docker logs ocariot-api-gateway --since "${TIME}"| grep -oE '\".*\" ' | awk '{print $2}')

TOUCHED_SERVICES=""
for SERVICE in ${MICROSERVICES}; do
	RESOURCES=$(cat ${YML_FILE_BASE} | yq r - "microservices.${SERVICE}.resources" | grep -vE '^ ' | sed 's@:.*@@g')
	for RESOURCE in ${RESOURCES}; do
		i=0
		COMMAND="yq r - microservices.${SERVICE}.resources.${RESOURCE}"
		while [ "$(cat ${YML_FILE_BASE} | ${COMMAND}.[${i}])" ]; do
			if [ "$(echo "${ROUTES}" | grep -E "$(cat ${YML_FILE_BASE} | ${COMMAND}.[${i}].path)")" ]; then
				DEPENDENCES=$(cat $2/docker-compose.yml \
					| sed 's@ \|:.*@@g' \
					| grep -e "^mongo-${SERVICE}$" \
						-e "^redis-${SERVICE}$" \
						-e "^mysql-${SERVICE}$")
				TOUCHED_SERVICES="${TOUCHED_SERVICES} ${SERVICE} ${DEPENDENCES}"
			fi
			i=$((i+1))
		done
	done
done

echo "$(echo ${TOUCHED_SERVICES} | sed 's@ @\n@g' | sort -u)" > $3

exit ${CODE}

