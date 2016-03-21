using UnityEngine;
using System.Collections;

public class InputHandler_OD1 : MonoBehaviour {

    // Update is called once per frame
    void Update () {

        if (Input.GetMouseButton(0))
        {
            Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
            RaycastHit rayCastHit;

            if (Physics.Raycast(ray.origin, ray.direction, out rayCastHit, Mathf.Infinity))
            {
                Debug.Log("Mouse Click!");
                ODoor1 door = rayCastHit.transform.GetComponent<ODoor1>();
                if(door)
                {
                    door.PlayDoor();
                }
            }
        }
	}
}
