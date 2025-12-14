@echo off
set BASE=src\main\java\com\tugasakhir\vendor_service

mkdir "%BASE%\kafka"
mkdir "%BASE%\external"
mkdir "%BASE%\exception"
mkdir "%BASE%\config"
mkdir "%BASE%\utils"

set DOMAINS=catalog rfq quotation delivery invoice report contract paymentterm evaluation notificationpref progressreport
set LAYERS=controller service repository model dto

for %%d in (%DOMAINS%) do (
    for %%l in (%LAYERS%) do (
        mkdir "%BASE%\%%l\%%d"
    )
)
