/// Created by wangzs on 2019-05-12 18:46

class User {
  String name;

  String data;

  String sex;

  int age;

  User({this.data, this.age, this.name, this.sex});

  User.fromJson(Map<dynamic, dynamic> modelJson)
      : name = modelJson['name'],
        data = modelJson['sex'],
        age = modelJson['age'],
        sex = modelJson['sex'];

  Map<dynamic, dynamic> toJson() => <String, dynamic>{
        'name': getNotNullString(name),
        'age': getNotNullInt(age),
        'data': getNotNullString(data),
        'sex': getNotNullString(sex),
      };

  getNotNullString(string) {
    return string == null ? "" : string;
  }

  getNotNullInt(int) {
    return int == null ? -1 : int;
  }

  @override
  String toString() {
    return 'User{name: $name, data: $data, sex: $sex, age: $age}';
  }

}
