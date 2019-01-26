
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
