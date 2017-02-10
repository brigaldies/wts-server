@echo off

REM --------------------------------------------------------
REM This simple Windows batch file can be used to deploy a
REM WTS_SERVER war built by Grails in the target directory
REM to a local Tomcat install.
REM
REM Usage: deploy-wts-server <version>
REM
REM The '<version>' must match the version property
REM in build.gradle
REM --------------------------------------------------------

IF NOT DEFINED WTS_SERVER_LOCAL_DEV_HOME       goto varErrors
IF NOT DEFINED CATALINA_HOME                   goto varErrors
IF NOT DEFINED WTS_SERVER_LOCAL_DEPLOY_HOME    goto varErrors
IF NOT DEFINED WTS_SERVER_LOCAL_DEPLOY_LOG_DIR goto varErrors

set wts-server-local-dev=%WTS_SERVER_LOCAL_DEV_HOME%
set tomcat-install=%CATALINA_HOME%
set wts-server-install=%WTS_SERVER_LOCAL_DEPLOY_HOME%
set wts-server-install-log-dir=%WTS_SERVER_LOCAL_DEPLOY_LOG_DIR%

IF NOT [%1]==[] (echo Deploying WTS_SERVER revision %1 from %wts-server-local-dev%\build\libs to %wts-server-install%) ELSE (goto usage)

REM Get user's confirmation
pause

REM --------------------------------------------------------
REM Shutdown Tomcat
REM --------------------------------------------------------
echo Shutdown Tomcat
echo on
call %tomcat-install%\bin\shutdown
echo ERRORLEVEL=%ERRORLEVEL%
IF NOT ERRORLEVEL 0 echo Warning: Tomcat was not shutdown successfully. Proceeding...
@echo off

REM --------------------------------------------------------
REM Build the war
REM --------------------------------------------------------
echo Build WTS-SERVER war file
echo on
cd %wts-server-local-dev%
call grails war -Dgrails.env=production -Dwts-server.log.base=/public/data/wts-server/ci/logs
@echo off

REM --------------------------------------------------------
REM Cleanup the deployment directory;
REM Copy the war file;
REM And explode the war file.
REM --------------------------------------------------------
echo on
del /S /Q %wts-server-install%\*.*
copy %wts-server-local-dev%\build\libs\wts-server-%1.war %wts-server-install%\.
cd %wts-server-install%
jar xvf wts-server-%1.war
@echo off

REM --------------------------------------------------------
REM Cleanup Tomcat's "work" area
REM --------------------------------------------------------
echo on
rmdir /S /Q %tomcat-install%\work\Catalina\localhost\wts-server
@echo off

REM --------------------------------------------------------
REM "Reset" the WTS-SERVER log file
REM --------------------------------------------------------
echo on
IF EXIST %wts-server-install-log-dir%\wts-server-%1.prev.log (
    rm %wts-server-install-log-dir%\wts-server-%1.prev.log
)
IF EXIST %wts-server-install-log-dir%\wts-server-%1.log (
    rename %wts-server-install-log-dir%\wts-server-%1.log %wts-server-install-log-dir%\wts-server-%1.prev.log
)
@echo off

REM --------------------------------------------------------
REM Start Tomcat
REM --------------------------------------------------------
echo Start Tomcat
echo on
call %tomcat-install%\bin\startup
@echo off

REM --------------------------------------------------------
REM We're done!
REM --------------------------------------------------------
echo Deployment of %wts-server-local-dev%\target\wts-server-%1.war completed on %date% at %time%
REM Get user's confirmation
pause
goto endScript

:varErrors
echo Error: Some environment variables are not set
IF DEFINED WTS_SERVER_LOCAL_DEV_HOME (echo WTS_SERVER_LOCAL_DEV_HOME=%WTS_SERVER_LOCAL_DEV_HOME%) ELSE (echo WTS_SERVER_LOCAL_DEV_HOME is not defined)
IF DEFINED CATALINA_HOME (echo CATALINA_HOME=%CATALINA_HOME%) ELSE (echo CATALINA_HOME is not defined)
IF DEFINED WTS_SERVER_LOCAL_DEPLOY_HOME (echo WTS_SERVER_LOCAL_DEPLOY_HOME=%WTS_SERVER_LOCAL_DEPLOY_HOME%) ELSE (echo WTS_SERVER_LOCAL_DEPLOY_HOME is not defined)
IF DEFINED WTS_SERVER_LOCAL_DEPLOY_LOG_DIR (echo WTS_SERVER_LOCAL_DEPLOY_LOG_DIR=%WTS_SERVER_LOCAL_DEPLOY_LOG_DIR%) ELSE (echo WTS_SERVER_LOCAL_DEPLOY_LOG_DIR is not defined)
goto endScript

:usage
echo Error: Please use the command usage below:
echo Usage: deploy-fdss version-to-deploy
goto endScript

:endScript
echo Script completed.