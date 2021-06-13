docker build ./ -t arm32v7/collector
docker image tag arm32v7/collector 192.168.180.122:5000/arm32v7/collector
docker push 192.168.180.122:5000/arm32v7/collector