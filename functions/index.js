const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Notification everytime a new invitation is sent
exports.sendNotification = functions.firestore.document('Invitations/{invite_id}').onCreate((change, context) => {

    // Every invite has a unique ID
    const invite_id = context.params.invite_id;

    //
    return admin.firestore().collection("Invitations").doc(invite_id).get().then(result => {

        const from = result.data().NameFrom;
        const to = result.data().NameTo;
        const to_user_id = result.data().ToUserID;
        const id_to_add_user_to = result.data().TeamIDtoAddto;

        return Promise.all([from, to, to_user_id]).then(res => {
            const from_name = from;
            const to_name = to;
            const dest_user_id = to_user_id;
            const team_ID = id_to_add_user_to;

            // Store the ID for the user receiveing the notificaition
            // Query the user name and store their user ID into the invition itseld
            // Now query here for the specific user ID and get the toekn ID
             return admin.firestore().collection("Users").doc(dest_user_id).get().then(device => {
                const token_id = device.data().deviceID;
                 //console.log("FROM: " + from_name + " TO: " + to_name);
                 console.log("TO USER ID: " + dest_user_id);

                 //payload
                             const payload = {
                                 notification: {
                                     title : "Notification From: " + from_name,
                                     body : from_name + " has invited you to join their team.",
                                     icon : "default",
                                     click_action : "com.example.cse110_wwr_team2.CLICK_NOTIFICATION"
                                 },
                                 data: {
                                    message : from_name + " has invited you to join their team.",
                                    dest_user_id : dest_user_id,
                                    invite_id : to_name,
                                    team_ID : team_ID
                                 }


                             };
                             return admin.messaging().sendToDevice(token_id, payload).then(result=> {
                                 return console.log("Notification sent");
                             });

                         });

                     });
            });




});


// Notification everytime a invitation is accepted, happens when status updated
exports.sendNotification2 = functions.firestore.document('Invitations/{invite_id}').onUpdate((change, context) => {

    // Every invite has a unique ID
    const invite_id = context.params.invite_id;

    //
    return admin.firestore().collection("Invitations").doc(invite_id).get().then(result => {

        const from = result.data().NameFrom;
        const to = result.data().NameTo;
        const to_user_id = result.data().ToUserID;
        const to_device_ID = result.data().DeviceID;

        return Promise.all([to, to_device_ID]).then(res => {
            const from_name = from;
            const to_name = to;
            const dest_device_ID = to_device_ID;

            //payload
            const payload = {
                notification: {
                    title : "Notification From: " + to_name,
                    body : to_name + " has accepted to join your team.",
                    icon : "default",
                    click_action : "com.example.cse110_wwr_team2.CLICK_ACCEPT_NOTIFICATION"
                }
            };
                return admin.messaging().sendToDevice(dest_device_ID, payload).then(result=>{
                    return console.log("Notification sent");
                 });

        });
    });
});

// Create proposed walks
exports.sendNotification4 = functions.firestore.document('ProposedRoutes/{route_id}').onCreate((change, context) => {

    // Every invite has a unique ID
    const route_id = context.params.route_id;

    //
    return admin.firestore().collection("ProposedRoutes").doc(route_id).get().then(result => {
        const team_id = result.data().teamID;
        const name = result.data().name;
        const start = result.data().startPoint;
        const date = result.data().dataTime;

        const topic = team_id;
        console.log("TO TEAM ID: " + topic);

        const payload = {
            notification:{
                title: "Update on Proposed Walks",
                body: name + " proposed a walk on: " + date + " at " + start + ".",
            },
            data : {
                click_action : "com.example.cse110_wwr_team2.CLICK_PROPOSE_NOTIFICATION"
            },
            topic : topic

        };
        return admin.messaging().send(payload).then((response) => {
                   // Response is a message ID string.
                   return console.log('Successfully sent message');

                 })
                 .catch((error) => {
                   return console.log('Error sending message', error);

                 });
    });
});


// Schedule/Withdraw/Update Walk.. ASSUME USER WILL NOT PROPOSE THE SAME WALK TWICE
exports.sendNotification5 = functions.firestore.document('ProposedRoutes/{route_id}').onUpdate((change, context) => {


    const route_id = context.params.route_id;

    //
    return admin.firestore().collection("ProposedRoutes").doc(route_id).get().then(result => {
        const team_id = result.data().teamID;
        const name = result.data().name;
        const start = result.data().startPoint;
        const date = result.data().dataTime;

        const topic = team_id;
        console.log("TO TEAM ID: " + topic);

        const payload = {
            notification:{
                title: "Proposed Walks UPDATE",
                body: " An UPDATE POSTED for proposed walk on: " + date + " at " + start + ".",
            },
            data : {
                click_action : "com.example.cse110_wwr_team2.CLICK_PROPOSE_NOTIFICATION"
            },
            topic : topic

        };
        return admin.messaging().send(payload).then((response) => {
                   // Response is a message ID string.
                   return console.log('Successfully sent message');

                 })
                 .catch((error) => {
                   return console.log('Error sending message', error);

                 });
    });
});

// Team member updates
exports.sendNotification6 = functions.firestore.document('ProposedRoutes/{route_id}/acceptMembers/{mem_ID}').onUpdate((change, context) => {

    // Every invite has a unique ID
    const route_id = context.params.route_id;
    const mem_id = context.params.mem_ID;

    //
    return admin.firestore().collection("ProposedRoutes").doc(route_id).get().then(result => {
        const team_id = result.data().teamID;
        const name = result.data().name;
        const start = result.data().startPoint;
        const date = result.data().dataTime;
        const id = mem_id;

        const topic = team_id;
        console.log("TO TEAM ID: " + topic);

         return admin.firestore().collection("Users").doc(id).get().then(result => {
            const name = result.data().name;

        const payload = {
            notification:{
                title: "Update on Proposed Walk from: " + name,
                body: name + "has changed their status for walk on: " + date + ".",
            },
            topic : topic

        };

        return admin.messaging().send(payload).then((response) => {
                   // Response is a message ID string.
                   return console.log('Successfully sent message');

                 })
                 .catch((error) => {
                   return console.log('Error sending message', error);

                 });
                 });
    });
});

//************** Currently have no way of implementing this.

// Notification everytime a invitation is declined, happens when Invitation is deleted
exports.sendNotification3 = functions.firestore.document('Invitations/{invite_id}').onDelete((change, context) => {
            const invite_id = change.before.val();
            console.log("BEFORE DATA:" + invite_id)
            return admin.firestore().collection("Invitations").doc(invite_id).get().then(result => {
                const to = result.data().to;

            //payload
            const payload = {
                notification: {
                    title : "Notification From you team.",
                    body : "Your team invitation has been declined. Click to see you team now.",
                    icon : "default",
                    click_action : "com.example.cse110_wwr_team2.CLICK_DECLINE_NOTIFICATION"
                }
            };
                return admin.messaging().sendToDevice(dest_user_id, payload).then(result=>{
                    return console.log("Notification sent");
                 });
                 });
});

