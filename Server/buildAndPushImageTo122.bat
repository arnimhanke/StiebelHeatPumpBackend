docker build ./ -t arm32v7/server
docker image tag arm32v7/server 192.168.180.122:5000/arm32v7/server
docker push 192.168.180.122:5000/arm32v7/server