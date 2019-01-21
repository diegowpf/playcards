
var initialState = {}

const ACTION_SWITCH_ACTIVE_CARD = "ACTION_SWITCH_ACTIVE_CARD";

export function handleNavigationClick(id) {
  console.log("Dispatching")
  return (dispatch) => {
    dispatch({type: ACTION_SWITCH_ACTIVE_CARD, payload: id});
  }
}

export default (state = initialState, action) => {
 switch (action.type) {
   case ACTION_SWITCH_ACTIVE_CARD:
     return {
       ...state,
       active_card_id: action.payload
     };
   default:
     return state;
 }
}
