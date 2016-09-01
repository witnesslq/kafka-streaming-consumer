#!/bin/bash
#

username="oneapm"
password="d0I63*6M5NEI"
address="123.57.22.56"

run () {
ftp -i -n -v ${address} << EOF
user ${username} ${password}
binary
hash
$1 $2
EOF
}

help ()
{
  echo "$0  -list | -get <要下载的文件>  | -put <要上传的文件>"
}

if [ -z $1 ];then
  help
fi

case $1 in
  "-list")
    run ls;exit ;;
  "-get")
    run get $2;exit ;;
  "-put")
    run put $2;exit ;;
  ?)
    help;exit ;;
esac
