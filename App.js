import React, { useEffect } from 'react';

import { View, NativeModules, Button, NativeEventEmitter, Alert } from 'react-native';

export default function App() {

  useEffect(() => {
    // console.log('NativeModules', NativeModules.Vouch);
  })

  const onPressButton = async () => {
    // const message = await Vouch.openChat('hello aayush');
    // alert(message)
    // Vouch.increment();
    // Alert.alert('Demo Alert', 'Yeahhhh!!')
    let LoadingOverlay = NativeModules.LoadingOverlay
    console.log(LoadingOverlay, NativeModules)
    LoadingOverlay.toggle(true)

    setTimeout(() => {
      LoadingOverlay.toggle(false)
    }, 3000)
  }

  return (
    <View>
      <Button title="Press" onPress={onPressButton} />
    </View>
  )
}