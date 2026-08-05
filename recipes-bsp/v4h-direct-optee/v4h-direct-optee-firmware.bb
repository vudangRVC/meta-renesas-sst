DESCRIPTION = "Packages and validates the Sparrow-Hawk BL31 and OP-TEE payloads for manual U-Boot loading"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require include/rz-optee-config.inc

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "rz-cmn"

DEPENDS = "trusted-firmware-a optee-os"

DIRECT_OPTEE_BL31 = "${RECIPE_SYSROOT}/boot/bl31-sparrow-hawk.bin"
DIRECT_OPTEE_TEE = "${RECIPE_SYSROOT}/boot/tee-raw-sparrow-hawk.bin"

do_install() {
    install -d ${D}/boot
    test -s ${DIRECT_OPTEE_BL31}
    test -s ${DIRECT_OPTEE_TEE}
    install -m 0644 ${DIRECT_OPTEE_BL31} ${D}/boot/bl31-sparrow-hawk.bin
    install -m 0644 ${DIRECT_OPTEE_TEE} ${D}/boot/tee-raw-sparrow-hawk.bin
}

python do_validate_payloads () {
    import binascii
    import os

    boot = os.path.join(d.getVar('D'), 'boot')
    payloads = (
        ('bl31-sparrow-hawk.bin', int(d.getVar('V4H_DIRECT_BL31_SIZE'), 0),
         d.getVar('V4H_DIRECT_BL31_CRC32')),
        ('tee-raw-sparrow-hawk.bin', int(d.getVar('V4H_DIRECT_TEE_SIZE'), 0),
         d.getVar('V4H_DIRECT_TEE_CRC32')),
    )
    for name, expected_size, expected_crc in payloads:
        path = os.path.join(boot, name)
        with open(path, 'rb') as payload:
            data = payload.read()
        size = len(data)
        crc32 = '%08x' % (binascii.crc32(data) & 0xffffffff)
        if size != expected_size or crc32 != expected_crc:
            bb.fatal('%s does not match the verified V4H contract: '
                     'size=%#x expected=%#x crc32=%s expected=%s' %
                     (name, size, expected_size, crc32, expected_crc))
}

addtask validate_payloads after do_install before do_package

FILES:${PN} = "/boot"
