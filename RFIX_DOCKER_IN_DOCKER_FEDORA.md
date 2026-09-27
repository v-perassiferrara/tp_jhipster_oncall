Correr esto dentro del container:

```bash
sudo update-alternatives --set iptables /usr/sbin/iptables-nft && \
sudo update-alternatives --set ip6tables /usr/sbin/ip6tables-nft && \
sudo pkill dockerd 2>/dev/null || true
sudo rm -f /var/run/docker.pid
sudo sh -c 'dockerd > /tmp/dockerd.log 2>&1 &'
sleep 5
docker ps
```
