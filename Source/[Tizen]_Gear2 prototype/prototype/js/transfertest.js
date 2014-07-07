function transferinit()
{
	var StringTransferButton,FileTransferButton;
	
	StringTransferButton = document.getElementById("string");
	FileTransferButton = document.getElementById("amrfile");
	
	StringTransferButton.addEventListener("click", onStringTransfer);
	FileTransferButton.addEventListener("click", onFileTransfer);
	
	function onStringTransfer()
	{
		try
		{
			g_SAPStringSock.sendData(g_SAAgent.channelIds[0],"Hello I'm gear 2");
		}catch(e)
		{
			console.log(e);
		}		
	}
	
	function onFileTransfer()
	{
		if(RecordedAudioPath !== undefined)
		{
			g_filetransfer.sendFile(g_peerAgent,'file://'+RecordedAudioPath);
		}
		else
		{
			alert("Target voice file path is undefined");
		}
	}
	
}