const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Notification everytime a new invitation is sent
exports.sendNotification = functions.firestore.document('Invitations/{invite_id}').onWrite((change, context) => {

    // Every invite has a unique ID
    const invite_id = context.params.invite_id;

    //
    return admin.firestore().collection("Invitations").doc(invite_id).get().then(result => {

        const from = result.data().NameFrom;
        const to = result.data().NameTo;

        return Promise.all([from, to]).then(res => {
            const from_name = from;
            const to_name = to;

            return admin.firestore().collection("Users").doc("ykU51X6iULSq3B6QX0wmRr1khAv1").get().then(device => {
                const token_id = device.data().deviceID;
                 console.log("FROM: " + from_name + " TO: " + to_name);

                 //payload
                             const payload = {
                                 notification: {
                                     title : "Notification From: " + from_name,
                                     body : from_name + " has invited you to join their team.",
                                     icon : "default"
                                 }
                             };
                             return admin.messaging().sendToDevice(token_id, payload).then(result=> {
                                 return console.log("Notification sent");
                             });

                         });

                     });
            });




});