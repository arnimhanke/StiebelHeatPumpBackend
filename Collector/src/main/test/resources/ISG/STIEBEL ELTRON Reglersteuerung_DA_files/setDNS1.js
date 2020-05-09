function SetDNS1()
{
	valueChanged = true;
	var sg0 = document.getElementById("netzwerk_sg_0");
	var sg1 = document.getElementById("netzwerk_sg_1");
	var sg2 = document.getElementById("netzwerk_sg_2");
	var sg3 = document.getElementById("netzwerk_sg_3");
	
	var dns0 = document.getElementById("netzwerk_dns1_0");
	var dns1 = document.getElementById("netzwerk_dns1_1");
	var dns2 = document.getElementById("netzwerk_dns1_2");
	var dns3 = document.getElementById("netzwerk_dns1_3");
	
	dns0.value= sg0.value;
	dns1.value= sg1.value;
	dns2.value= sg2.value;
	dns3.value= sg3.value;
	

}

function SetSG()
{
	valueChanged = true;
	var ip0 = document.getElementById("netzwerk_ip_0");
	var ip1 = document.getElementById("netzwerk_ip_1");
	var ip2 = document.getElementById("netzwerk_ip_2");
	
	
	var sg0 = document.getElementById("netzwerk_sg_0");
	var sg1 = document.getElementById("netzwerk_sg_1");
	var sg2 = document.getElementById("netzwerk_sg_2");
	
	var dns0 = document.getElementById("netzwerk_dns1_0");
	var dns1 = document.getElementById("netzwerk_dns1_1");
	var dns2 = document.getElementById("netzwerk_dns1_2");
	var dns3 = document.getElementById("netzwerk_dns1_3");
	
	sg0.value = ip0.value;
	sg1.value = ip1.value;
	sg2.value= ip2.value;
	
	dns0.value= sg0.value;
	dns1.value= sg1.value;
	dns2.value= sg2.value;
	dns3.value= sg3.value;
	

}
function GetTextLength(text)
{
	alert('hallo');
	var minwidth = text.Length;
	alert(minwidth);
}
