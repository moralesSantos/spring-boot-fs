const UserProfile = ({name,age, gender,imageNumber, ... props}) => {

    gender = gender === "Male" ? "men" : "women"

    return (
        <div>
            <p>{name}</p>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/med/${gender}/${imageNumber}.jpg`}/>
            {props.children}
        </div>
    )
}

export default UserProfile; 