using UnityEngine;
using System.Collections;

public class ODoor1 : MonoBehaviour {

    private int _mLastIndex;
    private AudioSource[] audioSource;
    private AudioSource door_open;
    private AudioSource door_close;


    public void PlayDoor()
    {
        audioSource = GetComponents<AudioSource>();
        door_open = audioSource[0];
        door_close = audioSource[1];

        if (!GetComponent<Animation>().isPlaying)
        {
            if (_mLastIndex == 0)
            {
                door_open.Play();
                GetComponent<Animation>().Play("ODoor1_open");
                _mLastIndex = 1;
            }
            else
            {
                door_close.Play();
                GetComponent<Animation>().Play("ODoor1_close");
                _mLastIndex = 0;
            }
        }
    }
}
