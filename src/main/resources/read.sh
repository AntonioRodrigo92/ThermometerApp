#!/bin/bash
bt=$(timeout 15 gatttool -b $1 --char-write-req --handle='0x0038' --value="0100" --listen)
if [ -z "$bt" ]
	then
		echo "The reading failed"
	else
		echo "Got data"
		#echo $bt 
		temphexa=$(echo $bt | awk -F ' ' '{print $12$11}'| tr [:lower:] [:upper:] )
		humhexa=$(echo $bt | awk -F ' ' '{print $13}'| tr [:lower:] [:upper:])
		temperature100=$(echo "ibase=16; $temphexa" | bc)
		humidity=$(echo "ibase=16; $humhexa" | bc)
		echo "scale=2;$temperature100/100"|bc
		echo $humidity
fi
