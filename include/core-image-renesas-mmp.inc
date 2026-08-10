IMAGE_INSTALL:append = " \
	packagegroup-gstreamer1.0-plugins \
	packagegroup-multimedia-libs \
	packagegroup-multimedia-kernel-modules \
	packagegroup-wayland-community \
	packagegroup-renesas-graphics \
"

# For GPU testing purposes
IMAGE_INSTALL:append = " kmscube glmark2 apitrace"
