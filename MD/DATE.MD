
Format specifier	Substitutes
````
%%	Character %
%#%	Character %, flag is ignored
%a	Abbreviated weekday name
%#a	Abbreviated weekday name, flag is ignored
%A	Full weekday name
%#A	Full weekday name, flag is ignored
%b	Abbreviated month name
%#b	Abbreviated month name, flag is ignored
%B	Full month name
%#B	Full month name, flag is ignored
%c	Date and time
%#c	Long date and time representation, appropriate for current locale. For example: Tuesday, March 14, 1995, 12:41:29
%d	Two-digit day of month (01 - 31)

%#d	Two-digit day of month, remove leading zeros (if any)
%H	Hour of the day, 24 hour day
%#H	Hour of the day, 24 hour day, remove leading zeros (if any)
%I	Two-digit hour, 12 hour day (01 - 12)
%#I	Two-digit hour, 12 hour day, remove leading zeros (if any)
%j	Three-digit day of year (001 - 366)
%#j	Three-digit day of year, remove leading zeros (if any)
%m	Two-digit month as a decimal number (1 - 12)
%#m	Two-digit month as a decimal number, remove leading zeros (if any)

%M	2-digit minute (00 - 59)
%#M	2-digit minute, remove leading zeros (if any)
%p	AM or PM
%#p	AM or PM, flag is ignored
%S	Two-digit second (00 - 59)
%#S	Two-digit second, remove leading zeros (if any)
%U	Two-digit week number where Sunday is the first day of the week (00 - 53)
%#U	Two-digit week number where Sunday is the first day of the week, remove leading zeros (if any)
%w	Weekday where 0 is Sunday (0 - 6)
%#w	Weekday where 0 is Sunday, remove leading zeros (if any)

%W	Two-digit week number where Monday is the first day of week the week (00 - 53)
%#W	Two-digit week number where Monday is the first day of week the week, remove leading zeros (if any)
%x	Date
%#x	Long date representation, appropriate to current locale. For example: Tuesday, March 14, 1995
%X	Time
%#X	Time, flag is ignored
%y	Two-digit year without century (00 to 99)
%#y	Two-digit year without century, remove leading zeros (if any)
%Y	Year with century
%#Y	Year with century, remove leading zeros (if any)

%Z, %z	Time zone name, or no characters if no time zone
%#Z	Time zone name, or no characters if no time zone, flag is ignored
```



Symbol	Meaning	Kind	Example
D	day in year	(Number)	189
E	day of week	(Text)	E/EE/EEE:Tue, EEEE:Tuesday, EEEEE:T
F	day of week in month	(Number)	2 (2nd Wed in July)
G	era designator	(Text)	AD
H	hour in day (0-23)	(Number)	0
K	hour in am/pm (0-11)	(Number)	0
L	stand-alone month	(Text)	L:1 LL:01 LLL:Jan LLLL:January LLLLL:J
M	month in year	(Text)	M:1 MM:01 MMM:Jan MMMM:January MMMMM:J
S	fractional seconds	(Number)	978
W	week in month	(Number)	2
Z	time zone (RFC 822)	(Time Zone)	Z/ZZ/ZZZ:-0800 ZZZZ:GMT-08:00 ZZZZZ:-08:00
a	am/pm marker	(Text)	PM
c	stand-alone day of week	(Text)	c/cc/ccc:Tue, cccc:Tuesday, ccccc:T
d	day in month	(Number)	10
h	hour in am/pm (1-12)	(Number)	12
k	hour in day (1-24)	(Number)	24
m	minute in hour	(Number)	30
s	second in minute	(Number)	55
w	week in year	(Number)	27
y	year	(Number)	yy:10 y/yyy/yyyy:2010
z	time zone	(Time Zone)	z/zz/zzz:PST zzzz:Pacific Standard Time
'	escape for text	(Delimiter)	'Date=':Date=
''	single quote	(Literal)	'o''clock':o'clock
 ———————————————— 
版权声明：本文为CSDN博主「谁是谁的学渣」的原创文章，遵循CC 4.0 by-sa版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/kluing/article/details/41553527