#! /usr/bin/perl
open IN, "<$ARGV[0]";
open OUT, ">TEMP";
$flag=0;
while(<IN>)
{
	if(/<uses-permission /)
	{
		if($flag==0)
		{
			print OUT "    <uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\" />";
			$flag=1;
		}
	}
	print OUT $_;
} 
