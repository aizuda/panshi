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
        authorization: `${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    })
      .then(async response => {
        // 获取 Content-Type
        const contentType = response.headers.get('content-type') || '';

        // 判断是否为 JSON 错误响应
        if (contentType.includes('application/json')) {
          const errorData = await response.json();
          const errorMessage = errorData.message || errorData.msg || '操作失败';
          window.$message?.error(errorMessage);
          return;
        }

        // 正常的 ZIP 文件流
        if (response.ok) {
          const data = await response.blob();
          downloadByData(data, fileName, 'application/zip');
        } else {
          // 非 JSON 格式的错误响应
          window.$message?.error(`下载失败: ${response.status} ${response.statusText}`);
        }
      })
      .catch(err => {
        window.$message?.error(err.message || '网络错误');
      });
  }

  return {
    zip
  };
}
