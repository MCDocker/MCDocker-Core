#!/bin/bash

CONTENT="[Desktop Entry]
Type=Application
Name=MCDocker
Exec=/bin/sh \"$1 %u\"
Icon=$HOME/.mcdocker/icon.png
Categories=Game
MimeType=x-scheme-handler/mcdocker"

echo "$CONTENT" > /usr/share/applications/mcdocker.desktop
