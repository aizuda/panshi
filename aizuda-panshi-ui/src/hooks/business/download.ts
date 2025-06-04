import { localStg } from '@/utils/storage';
import { getServiceBaseURL } from '@/utils/service';

export function useDownload() {
  const isHttpProxy = import.meta.env.DEV && import.meta.env.VITE_HTTP_PROXY === 'Y';
  const { baseURL } = getServiceBaseURL(import.meta.env, isHttpProxy);

  function downloadByData(data: BlobPart, filename: string, type: string = 'application/octet-stream') {
    const blobData = [data];
    const blob = new Blob(blobData, { type });

    const blobURL = window.URL.createObjectURL(blob);
    const tempLink = document.createElement('a');
    tempLink.style.display = 'none';
    tempLink.href = blobURL;
    tempLink.setAttribute('download', filename);
    if (typeof tempLink.download === 'undefined') {
      tempLink.setAttribute('target', '_blank');
    }
    document.body.appendChild(tempLink);
    tempLink.click();
    document.body.removeChild(tempLink);
    window.URL.revokeObjectURL(blobURL);
  }

  function zip(url: string, fileName: string, body: Record<string, any>) {
    const token = localStg.get('token');
    const now = Date.now();
    fetch(`${baseURL}${url}${url.includes('?') ? '&' : '?'}t=${now}`, {
      method: 'post',
      headers: {
        accesstoken: `${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    })
      .then(response => response.blob())
      .then(data => downloadByData(data, fileName, 'application/zip'))
      .catch(err => window.$message?.error(err.message));
  }

  return {
    zip
  };
}
