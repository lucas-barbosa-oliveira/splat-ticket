#!/usr/bin/env bash

MAP_SERVICES="account 3001
mongo-account 27018
iot-tracking 4001
mongo-iot-tracking 27019
ds-agent 5001
mongo-ds-agent 27020
quest 6001
mongo-quest 27021
gamification 7001
mongo-gamification 27022
missions 8001
mysql-missions 3306
food 9001
mongo-food 27023
notification 10001
mongo-notification 27024"

SERVICES=$@

if [ "$1" == "all" ]; then
	SERVICES=$(echo "$MAP_SERVICES" | awk '{print $1}')
fi

for SERVICE in ${SERVICES}; do
	if [ -z "$(echo ${MAP_SERVICES} | grep ${SERVICE})" ]; then
		continue
	fi
	UP_SERVICE="false"
	while [ ${UP_SERVICE} == "false" ]; do
		PORT=$(echo "${MAP_SERVICES}" | grep ${SERVICE} | awk '{print $2}')
		nc -vz localhost ${PORT} &> /dev/null
		if [ $? -eq 0 ]; then
			echo "Service ${SERVICE} UP."
			UP_SERVICE=true
		else
			echo "Service ${SERVICE} DOWN."
		fi
		sleep 1
	done
done



