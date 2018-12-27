var int n;
var bool t;
t = false;
while (!t){
	t = !t;
	if (t)
		n = ++n;
}
prompt(n);
