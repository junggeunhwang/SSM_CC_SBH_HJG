var g_filetransfer;

function SAPFileInit()
{
	try { 
		g_filetransfer = g_SAAgent.getSAFileTransfer(); 
	} catch(e) { 
		console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
	} 
	// 보내는 콜백
	var sendfilecallback = { 
			onprogress : function(transferId, progress)
			{ 
				console.log("onprogress transferId : " + transferId + ", progress : " + progress); 
			}, 
			oncomplete : function(transferId, localPath)
			{ 
				console.log("File transfer complete. transferId : " + transferId);
				mainExtrastatus.innerHTML = msg_audiosendingsuccess;
			}, 
			onerror : function(errorCode, transferId)
			{ 
				console.log("FileSendError transferId : " + transferId + " code : " + errorCode);
				mainExtrastatus.innerHTML = msg_audiosendingerror;
			} 
	}; 
	// 받는 콜백
	var receivefilecallback = { 
			onreceive : function(transferId, fileName)
			{ 
				console.log("Incoming file transfer request form the remote peer agent. transferId : " + transferId 
						+ " file name : " + fileName);
				var date = new Date();
				var prefix = "multicastedVoice" + date.getHours().toString()+"_" + date.getMinutes().toString()+"_" + date.getSeconds().toString()+"_"+date.getMilliseconds();
				var newFileName = prefix+"kqpq"+fileName;
				var URIFilePath = 'file://'+'/opt/usr/media/Downloads/'+newFileName;				
				
				console.log(URIFilePath);
				
				try
				{
					g_filetransfer.receiveFile(transferId,URIFilePath);
				}catch(e)
				{
					console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
				}
			}, 
			onprogress : function(transferId, progress){ 
				console.log("onprogress transferId : " + transferId + ", progress : " + progress); 
			}, 
			oncomplete : function(transferId, localPath){ 
				console.log("File transfer complete. transferId : " + transferId + "path : "+localPath);
				
				var queuedata={};
				var filename;
				
				queuedata.type = "file";
				
				try
				{
					queuedata.path = filesystem.getSystemPath(localPath);
				
					filename = filesystem.getFileNameWithoutExtension(localPath);
					
					queuedata.sender = filename.slice(filename.indexOf("kqpq")+4);
				}catch(e)
				{
					console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
				}
				
				console.log(queuedata.name);
				
				multicastedqueue.push(queuedata);
				
			}, 
			onerror : function(errorCode, transferId){ 
				console.log("FileReceiveError transferId : " + transferId + " code : " + errorCode); 
			} 
	};
	try 
	{				 
		g_filetransfer.setFileSendListener(sendfilecallback);
		g_filetransfer.setFileReceiveListener(receivefilecallback); 
	} catch(e) 
	{ 
		console.log("Error Exception, error name : " + e.name + ", error message : " + e.message); 
	}
}