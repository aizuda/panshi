import { createRequire } from 'node:module';
import monacoEditorPlugin from 'vite-plugin-monaco-editor-esm';

const require = createRequire(import.meta.url);

export function setupMonacoEditorPlugin() {
  return monacoEditorPlugin({
    // Resolve Monaco's public worker exports explicitly for modern Monaco releases.
    languageWorkers: [],
    customWorkers: [
      { label: 'editorWorkerService', entry: require.resolve('monaco-editor/editor/editor.worker') },
      { label: 'css', entry: require.resolve('monaco-editor/language/css/css.worker') },
      { label: 'html', entry: require.resolve('monaco-editor/language/html/html.worker') },
      { label: 'json', entry: require.resolve('monaco-editor/language/json/json.worker') },
      { label: 'typescript', entry: require.resolve('monaco-editor/language/typescript/ts.worker') }
    ]
  });
}
