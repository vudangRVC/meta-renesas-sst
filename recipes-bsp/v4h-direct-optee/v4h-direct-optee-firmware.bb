DESCRIPTION = "Sparrow-Hawk direct OP-TEE boot payload"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "rz-cmn"

DEPENDS = "trusted-firmware-a optee-os python3-native"
do_install[depends] += "trusted-firmware-a:do_deploy optee-os:do_deploy"

inherit python3native

DIRECT_OPTEE_BL31 = "${DEPLOY_DIR_IMAGE}/target/images/atf/bl31-sparrowhawk.bin"
DIRECT_OPTEE_TEE = "${DEPLOY_DIR_IMAGE}/target/images/atf/tee-raw-sparrow-hawk.bin"

do_install() {
    install -d ${D}/boot
    test -s ${DIRECT_OPTEE_BL31}
    test -s ${DIRECT_OPTEE_TEE}
    install -m 0644 ${DIRECT_OPTEE_BL31} ${D}/boot/bl31-sparrow-hawk.bin
    install -m 0644 ${DIRECT_OPTEE_TEE} ${D}/boot/tee-raw-sparrow-hawk.bin
}

python do_generate_manifest () {
    import binascii
    import hashlib
    import json
    import os

    boot = os.path.join(d.getVar('D'), 'boot')
    payloads = (
        ('bl31', 'bl31-sparrow-hawk.bin', 0x46400000, 0x22200),
        ('tee', 'tee-raw-sparrow-hawk.bin', 0x44100000, 0x300000),
    )
    manifest = {'format': 1, 'payloads': {}}

    for key, name, address, maximum in payloads:
        path = os.path.join(boot, name)
        with open(path, 'rb') as payload:
            data = payload.read()
        size = len(data)
        if not size or size > maximum:
            bb.fatal('%s has invalid size %d (maximum %#x)' % (name, size, maximum))
        manifest['payloads'][key] = {
            'file': '/boot/' + name,
            'load_address': '0x%08x' % address,
            'size': size,
            'crc32': '%08x' % (binascii.crc32(data) & 0xffffffff),
            'sha256': hashlib.sha256(data).hexdigest(),
        }

    env_path = os.path.join(boot, 'v4h-direct-optee.env')
    with open(env_path, 'w', encoding='ascii') as env:
        env.write('v4h_manifest_version=1\n')
        for key in ('bl31', 'tee'):
            item = manifest['payloads'][key]
            env.write('%s_file=%s\n' % (key, item['file']))
            env.write('%s_addr=%s\n' % (key, item['load_address']))
            env.write('%s_size=0x%x\n' % (key, item['size']))
            env.write('%s_crc32=%s\n' % (key, item['crc32']))

    manifest_path = os.path.join(boot, 'v4h-direct-optee.manifest')
    with open(manifest_path, 'w', encoding='ascii') as output:
        json.dump(manifest, output, indent=2, sort_keys=True)
        output.write('\n')

    os.chown(env_path, 0, 0)
    os.chown(manifest_path, 0, 0)
}

addtask generate_manifest after do_install before do_package
do_generate_manifest[fakeroot] = "1"

FILES:${PN} = "/boot"
