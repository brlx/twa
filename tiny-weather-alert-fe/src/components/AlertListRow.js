'use strict';
import React, {PropTypes} from 'react';

const AlertListRow = ({alert, deleteAction, busy}) => {
   return (
      <tr>
         <td>{alert.city.name + ' (' + alert.city.country + ')'}</td>
         <td>{
            alert.currentTemp
               ? alert.currentTemp + '\u00B0C'
               : ''
         }</td>
         <td>{alert.threshold + '\u00B0C'}</td>
         <td>{alert.todayTriggered ? <span className="label label-danger">ALERT TODAY</span> : ''}</td>
         <td>
            {/*<button type="button" className="btn">Edit</button>*/}
            <button type="button" className="btn btn-danger btn-xs"
                    onClick={deleteAction} disabled={busy} >
               Delete
            </button>
         </td>
      </tr>
   );
};

AlertListRow.propTypes = {
   alert: PropTypes.object.isRequired,
   deleteAction: PropTypes.func.isRequired,
   busy: PropTypes.bool.isRequired
};

export default AlertListRow;
