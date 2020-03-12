import React, { useEffect } from 'react';

import { View, NativeModules, Button, NativeEventEmitter } from 'react-native';

const Vouch = NativeModules.Vouch;

export default function App() {

  useEffect(() => {
    console.log('NativeModules', NativeModules.Vouch);
  })

  const onPressButton = async () => {
    // const message = await Vouch.openChat('hello aayush');
    // alert(message)
    Vouch.increment();
  }

  return (
    <View>
      <Button title="Press" onPress={onPressButton} />
    </View>
  )
}