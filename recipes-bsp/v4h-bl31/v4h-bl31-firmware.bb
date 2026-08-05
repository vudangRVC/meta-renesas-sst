DESCRIPTION = "Packages the mandatory Sparrow-Hawk BL31 secure monitor and generated U-Boot validation metadata"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require include/rz-optee-config.inc

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "rz-cmn"

DEPENDS = "trusted-firmware-a python3-native"

inherit python3native

V4H_BL31_SOURCE = "${RECIPE_SYSROOT}/boot/bl31-sparrow-hawk-nooptee.bin"

do_install() {
    install -d ${D}/boot
    test -s ${V4H_BL31_SOURCE}
    install -m 0644 ${V4H_BL31_SOURCE} \
        ${D}/boot/bl31-sparrow-hawk-nooptee.bin
}

python do_generate_manifest() {
    import binascii
    import hashlib
    import json
    import os

    boot = os.path.join(d.getVar('D'), 'boot')
    name = 'bl31-sparrow-hawk-nooptee.bin'
    path = os.path.join(boot, name)
    with open(path, 'rb') as payload:
        data = payload.read()

    size = len(data)
    crc32 = '%08x' % (binascii.crc32(data) & 0xffffffff)
    address = 0x46400000
    item = {
        'file': '/boot/' + name,
        'load_address': '0x%08x' % address,
        'size': size,
        'crc32': crc32,
        'sha256': hashlib.sha256(data).hexdigest(),
    }
    manifest = {
        'format': 1,
        'purpose': 'mandatory Sparrow-Hawk PSCI provider without BL32',
        'tfa_v4h': d.getVar('V4H_DIRECT_TFA_V4H_SRCREV'),
        'payload': item,
    }

    env_path = os.path.join(boot, 'v4h-bl31.env')
    with open(env_path, 'w', encoding='ascii') as env:
        env.write('v4h_bl31_file=%s\n' % item['file'])
        env.write('v4h_bl31_addr=%s\n' % item['load_address'])
        env.write('v4h_bl31_size=0x%x\n' % item['size'])
        env.write('v4h_bl31_crc32=%s\n' % item['crc32'])

    manifest_path = os.path.join(boot, 'v4h-bl31.manifest')
    with open(manifest_path, 'w', encoding='ascii') as output:
        json.dump(manifest, output, indent=2, sort_keys=True)
        output.write('\n')

    os.chown(env_path, 0, 0)
    os.chown(manifest_path, 0, 0)
}

addtask generate_manifest after do_install before do_package
do_generate_manifest[fakeroot] = "1"

FILES:${PN} = "/boot"
