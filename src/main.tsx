import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import './main.css'
import {GoalEditorPane} from "./components/Goals/GoalEditor.tsx";

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    {/*<MessagePane/>*/}
    {/*<Test/>*/}
    {/*<TemplatePane/>*/}
    <GoalEditorPane/>
  </React.StrictMode>,
)
