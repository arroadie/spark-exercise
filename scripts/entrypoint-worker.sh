#!/bin/bash
set -xe

## Defaults
#
: ${SPARK_HOME:?must be set!}

## Load spark-env.sh Spark environment configuration variables
SPARK_CONF_DIR="${SPARK_CONF_DIR:-"${SPARK_HOME}/conf"}"
[ ! -f ${SPARK_CONF_DIR}/spark-env.sh ] || . ${SPARK_CONF_DIR}/spark-env.sh

## Use the given domain name or hostname for naming a Spark node
#
if [ -n "${SPARK_DOMAIN}" ]; then
  host_opts="-h $(hostname -s).${SPARK_DOMAIN}"
elif [ -n "${SPARK_HOSTNAME}" ]; then
  host_opts="-h ${SPARK_HOSTNAME}"
fi

## Invocation shortcut commands
#
cmd=$1
case $cmd in
master|worker)
    shift
    echo "Starting worker"
    set -- ${SPARK_HOME}/bin/spark-class "org.apache.spark.deploy.worker.Worker" -c 4 -m 2G spark://sparkmaster:7077
    exec /tini -- $@ $host_opts
  ;;
spark-shell|shell)
    shift
    exec /tini -- ${SPARK_HOME}/bin/spark-shell $@
  ;;
*)
    # Run an arbitary command
    [ -n "$*" ] && exec $@ || exec /bin/bash
  ;;
esac