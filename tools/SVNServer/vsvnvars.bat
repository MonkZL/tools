@echo off

set PATH=%~dp0bin;%PATH%

IF "%1" == "" GOTO welcome
%*

GOTO end

:welcome
echo Welcome to VisualSVN Server command prompt!
echo Use 'svn help', 'svnadmin help' and 'svnlook help' for more information.
echo Type 'exit' to exit.
GOTO end

:end