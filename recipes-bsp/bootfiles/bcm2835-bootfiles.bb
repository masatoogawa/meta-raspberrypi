DESCRIPTION = "Closed source binary files to help boot the ARM on the BCM2835."
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=4a4d169737c0786fb9482bb6d30401d1"

inherit deploy package

include recipes-bsp/common/firmware.inc

INHIBIT_DEFAULT_DEPS = "1"

DEPENDS = "rpi-config"

COMPATIBLE_MACHINE = "^rpi$"

S = "${RPIFW_S}/boot"

PR = "r3"

INSANE_SKIP_${PN} = "dev-elf ldflags"
INSANE_SKIP_${PN}-dev = "dev-elf ldflags"

do_install() {

   install -d ${D}${includedir}/IL
   install -d ${D}${libdir}/
   install -m 0644 ${S}/../opt/vc/include/IL/*.h ${D}${includedir}/IL
   install -m 0755 ${S}/../opt/vc/lib/*.so ${D}${libdir}/

   # remove duplicated library with mesa

   rm ${D}${libdir}/libWFC.so
   rm ${D}${libdir}/libOpenVG.so
   rm ${D}${libdir}/libGLESv1_CM.so
   rm ${D}${libdir}/libEGL.so
   rm ${D}${libdir}/libGLESv2.so
}


do_deploy() {
    install -d ${DEPLOYDIR}/${PN}

    for i in ${S}/*.elf ; do
        cp $i ${DEPLOYDIR}/${PN}
    done
    for i in ${S}/*.dat ; do
        cp $i ${DEPLOYDIR}/${PN}
    done
    for i in ${S}/*.bin ; do
        cp $i ${DEPLOYDIR}/${PN}
    done

    # Add stamp in deploy directory
    touch ${DEPLOYDIR}/${PN}/${PN}-${PV}.stamp
}

addtask deploy before do_build after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${PN}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES_${PN} += "${libdir}/*.so"
FILES_SOLIBSDEV = ""

