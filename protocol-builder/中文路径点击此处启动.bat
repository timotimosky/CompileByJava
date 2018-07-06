@echo off
rem Script to launch the command line interface for protocol builder
set PROTOCOL_HOME=%~dp0app
set JAVA_EXEC=%~dp0runtime\jre\bin\javaw
set CP=%PROTOCOL_HOME%\*;%PROTOCOL_HOME%\lib\*;
start %JAVA_EXEC% -cp "%CP%" com.common.builder.launcher.ProtocolBuilderLauncher
@if errorlevel 1 off