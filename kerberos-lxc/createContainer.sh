#!/usr/bin/env bash

lxc-create --template download --name kerberos -- --dist debian --release jessie --arch amd64

cp kerberos-lxc/configureContainer.sh /var/lib/lxc/kerberos/rootfs/tmp/
chroot /var/lib/lxc/kerberos/rootfs /tmp/configureContainer.sh

echo -e "\n\nStarting the Kerberos container"
lxc-start --name kerberos --daemon

echo -e "\nSleeping for 30s to allow the container to start"
sleep 30

tail /var/lib/lxc/kerberos/rootfs/var/log/kadmin.log

dpkg -l | grep krb5


# We must configure kerberos on the local machine so we can use kadmin and kinit commands

REALM="EXAMPLE.COM"
DOMAIN="example.com"
CONTAINER_IP=$(lxc-info -n kerberos -iH)

echo -e "\nContainer IP: $CONTAINER_IP"

cat > /etc/krb5.conf <<EOF
[libdefaults]
	default_realm = $REALM

[realms]
	$REALM = {
		kdc = $CONTAINER_IP:88
		admin_server = $CONTAINER_IP:749
		default_domain = $DOMAIN
	}
[domain_realm]
	.$DOMAIN = $REALM
	$DOMAIN = $REALM
EOF

echo -e "\nTrying kinit kadmin/admin@EXAMPLE.TEST (should fail)"
kinit kadmin/admin@EXAMPLE.TEST <<EOF
MITiys4K5!
EOF

echo -e "\nTrying kinit kadmin/admin@EXAMPLE.COM (should work)"
kinit kadmin/admin@EXAMPLE.COM <<EOF
MITiys4K5!
EOF

klist && echo -e "\nKerberos fully operational"



kadmin -p kadmin/admin@EXAMPLE.COM -q "getprincipal kadmin/admin@EXAMPLE.COM" <<EOF
MITiys4K5!
EOF