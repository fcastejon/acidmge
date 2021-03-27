#!/usr/bin/perl

$ini=0.006;
$fin=0.016;
$step=0.0025;

for($muta=$ini; $muta <= $fin; $muta += $step)
{
	$cini=0.1;
	$cfin=0.9;
	$cstep=0.2;

	for($cruce=$cini; $cruce <= $cfin; $cruce += $cstep)
	{
	@list = ( 20, 50, 75, 100, 150 );

	foreach $medgenes (@list)
	{
		for($w=0; $w < 400; $w++)
		{
		print "ejecutar.sh -m $muta -c $cruce -i $medgenes\n";
		system("./ejecutar.sh -m $muta -c $cruce -i $medgenes");
		}
	}
	}
}





