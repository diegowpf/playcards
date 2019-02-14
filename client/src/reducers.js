import axios from 'axios'
var initialState = {}

const ACTION_SWITCH_ACTIVE_CARD = "ACTION_SWITCH_ACTIVE_CARD";
const ACTION_UPLOAD_PROGRESS_CHANGE = "ACTION_UPLOAD_PROGRESS_CHANGE";
const ACTION_UPLOAD_FILE = "ACTION_UPLOAD_FILE";

export function handleNavigationClick(id) {
  console.log("Dispatching")
  return (dispatch) => {
    dispatch({type: ACTION_SWITCH_ACTIVE_CARD, payload: id});
  }
}

export function uploadFile(file){
  console.log("Updating progress" )

  var formData = new FormData();

  console.log("This is the file", file );
  formData.append("file",file);
  axios.post('http://server.immersivesports.ai/playcards/team/import/c679919f-d524-3f75-ad2a-5161706e12a5', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
})

  return (dispatch) => {
    dispatch({type: ACTION_UPLOAD_FILE, payload: file });
  }
}

export default (state = initialState, action) => {
 switch (action.type) {
   case ACTION_SWITCH_ACTIVE_CARD:
     return {
       ...state,
       active_card_id: action.payload
     };
     case ACTION_UPLOAD_PROGRESS_CHANGE:
       return {
         ...state,
         progress: action.payload
       };
       case ACTION_UPLOAD_FILE:
         return {
           ...state,
           file: action.payload
         };
   default:
     return state;
 }
}
