SECTION = "bootloaders"
SUMMARY = "Firmware Packaging"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

inherit deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "trusted-firmware-a u-boot"
DEPENDS += " bptool-native fiptool-native"

UNPACKDIR = "${S}"

do_configure[noexec] = "1"

do_compile () {
	# Create bl2_bp.bin
	bptool ${RECIPE_SYSROOT}/boot/bl2-${MACHINE}.bin ${S}/bp.bin 0x08103000 spi
	cat ${S}/bp.bin ${RECIPE_SYSROOT}/boot/bl2-${MACHINE}.bin > ${S}/bl2_bp_spi.bin

	machine="${@d.getVar('MACHINE', True)}"
	if [ "${machine}" != "rzv2h-evk-ver1" ]; then
		bptool ${RECIPE_SYSROOT}/boot/bl2-${MACHINE}.bin ${S}/bp.bin 0x08103000 mmc
		cat ${S}/bp.bin ${RECIPE_SYSROOT}/boot/bl2-${MACHINE}.bin > ${S}/bl2_bp_emmc.bin
	fi
	bptool ${RECIPE_SYSROOT}/boot/bl2-${MACHINE}.bin ${S}/bp.bin 0x08103000 esd
	cat ${S}/bp.bin ${RECIPE_SYSROOT}/boot/bl2-${MACHINE}.bin > ${S}/bl2_bp_esd.bin

	# Create fip.bin
	fiptool create --align 16 --soc-fw ${RECIPE_SYSROOT}/boot/bl31-${MACHINE}.bin \
		--nt-fw ${RECIPE_SYSROOT}/boot/u-boot.bin ${S}/fip.bin

	# Convert to srec
	objcopy -I binary -O srec --adjust-vma=0x08101E00 --srec-forceS3 ${S}/bl2_bp_spi.bin ${S}/bl2_bp_spi.srec
	if [ "${machine}" != "rzv2h-evk-ver1" ]; then
		objcopy -I binary -O srec --adjust-vma=0x08101E00 --srec-forceS3 ${S}/bl2_bp_emmc.bin ${S}/bl2_bp_emmc.srec
	fi
	objcopy -I binary -O srec --adjust-vma=0x08101E00 --srec-forceS3 ${S}/bl2_bp_esd.bin ${S}/bl2_bp_esd.srec
	objcopy -I binary -O srec --adjust-vma=0x44000000 --srec-forceS3 ${S}/fip.bin ${S}/fip.srec
}

do_deploy () {
	# Create deploy folder
	install -d ${DEPLOYDIR}/target/images

	# Copy BL2 and fip images
	install -m 0644  ${S}/bl2_bp_spi.bin ${DEPLOYDIR}/target/images/bl2_bp_spi-${MACHINE}.bin
	install -m 0644  ${S}/bl2_bp_spi.srec ${DEPLOYDIR}/target/images/bl2_bp_spi-${MACHINE}.srec

	machine="${@d.getVar('MACHINE', True)}"
	if [ "${machine}" != "rzv2h-evk-ver1" ]; then
		install -m 0644  ${S}/bl2_bp_emmc.bin ${DEPLOYDIR}/target/images/bl2_bp_emmc-${MACHINE}.bin
		install -m 0644  ${S}/bl2_bp_emmc.srec ${DEPLOYDIR}/target/images/bl2_bp_emmc-${MACHINE}.srec
	fi
	install -m 0644  ${S}/bl2_bp_esd.bin ${DEPLOYDIR}/target/images/bl2_bp_esd-${MACHINE}.bin
	install -m 0644  ${S}/bl2_bp_esd.srec ${DEPLOYDIR}/target/images/bl2_bp_esd-${MACHINE}.srec

	install -m 0644  ${S}/fip.bin ${DEPLOYDIR}/target/images/fip-${MACHINE}.bin
	install -m 0644  ${S}/fip.srec ${DEPLOYDIR}/target/images/fip-${MACHINE}.srec
}

addtask deploy before do_build after do_compile