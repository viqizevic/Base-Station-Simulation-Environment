#first model: global clusters

#file
param file := "toy5.scn";

#sets:
param noBS := read file as "1n" use 1 comment "#";
param noMS := read file as "1n" skip 1 use 1 comment "#";
set M:={1 to noBS};
set N:={1 to noMS};

#vars
var a[<n,b> in N*M] binary;
var c[<b1,b2> in M*M with b1<b2] binary;

#params
param totalPower[M] := read file as "<1n> 2n" skip 2 use noBS comment "#";
param K[M] := read file as "<1n> 2n" skip (2+noBS) use noBS comment "#";
param H[N*M] := read file as "<1n,2n> 3n" skip (2+2*noBS) use (noBS*noMS) comment "#";
param gamma[N] := read file as "<1n> 2n" skip (2+noBS*(2+noMS)) use noMS comment "#";
param sigma2[N] := read file as "<1n> 2n" skip (2+noBS*(2+noMS)+noMS) use noMS comment "#";
param r[<u,n,b> in N*N*M] := (1/sigma2[u])*H[u,b]*((totalPower[b])/(K[b]))* if (n==u) then 1/gamma[u] else -1 end;

#obj function
minimize globalCoop: sum<b1,b2> in M*M with b1<b2: c[b1,b2];

#constraints
subto sinr: forall<u> in N: sum<n,b> in N*M: r[u,n,b]*a[n,b] >= 1;
subto power: forall<b> in M: sum<u> in N: a[u,b] <= K[b];
subto cooperation: forall<u,b1,b2> in N*M*M with b1<b2: c[b1,b2] + 1 >= a[u,b1] + a[u,b2];
subto completeCluster1: forall<b1,b2,b3> in M*M*M with b1<b2 and b2<b3: c[b1,b2] + 1 >= c[b1,b3] + c[b2,b3];
subto completeCluster2: forall<b1,b2,b3> in M*M*M with b1<b2 and b2<b3: c[b1,b3] + 1 >= c[b1,b2] + c[b2,b3];
subto completeCluster3: forall<b1,b2,b3> in M*M*M with b1<b2 and b2<b3: c[b2,b3] + 1 >= c[b1,b2] + c[b1,b3];

